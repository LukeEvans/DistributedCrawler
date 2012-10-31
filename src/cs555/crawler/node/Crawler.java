package cs555.crawler.node;

import cs555.crawler.peer.Peer;
import cs555.crawler.pool.FetchParseTask;
import cs555.crawler.pool.ThreadPoolManager;
import cs555.crawler.url.CrawlerState;
import cs555.crawler.url.Page;
import cs555.crawler.utilities.Constants;
import cs555.crawler.utilities.Tools;
import cs555.crawler.wireformatsURL.DomainRequest;
import cs555.crawler.wireformatsURL.URLRequest;
import cs555.crawler.wireformatsURL.URLResponse;

public class Crawler {

	PeerNode peer;
	int depth;
	CrawlerState crawlState;
	ThreadPoolManager poolManager;

	//================================================================================
	// Init
	//================================================================================
	public Crawler(int d, int p, int i, int r) {
		depth = d;
		peer = new PeerNode(p, i, r, this);
		crawlState = new CrawlerState();
	}

	public void init(String d, int p){
		peer.initServer();
		peer.enterDHT(d, p);
		poolManager = new ThreadPoolManager(Constants.Threads);
		poolManager.start();
	}

	//================================================================================
	// Publish
	//================================================================================
	public void publishLink(URLRequest req) {
		
		// We've met our maximum depth. Don't continue
		if (req.depth == depth) {
			return;
		}
		
		synchronized (crawlState) {
			Page page = new Page(req.url, req.depth, req.domain);

			// If we have not seen page yet, add it to our state as ready
			if (crawlState.addPage(page)) {
				peer.publishLink(req);
				crawlState.markUrlPending(page);
			}	

		}
	}

	public void handlePage(URLResponse response, boolean domainLeader) {

		synchronized (crawlState) {
			// If we're not in charge of this domain, send back to intermediary
			if (!domainLeader) {
				Peer intermediary = new Peer(response.intermediaryHostName, response.intermediaryPort);
				peer.sendResponse(intermediary, response);
			}

			// If we are, handle it
			else {
				// mark the response page as complete
				Page page = new Page(response.url, response.depth, response.domain);
				crawlState.markUrlPending(page);
				
				// Accumulate data
				crawlState.accumulateData(response);
				
				

				// for each link, if it belongs to our domain, continue processesing or hand it off to another leader
				for (String s : response.links) {

					URLRequest req = response.getNextLevelRequest(s);

					if (s.contains("." + response.domain)) {
						publishLink(req);
					}

					// hand it off to someone else
					else {
						req.type = Constants.Handoff_Request;
						peer.publishHandoff(req);
					}
				}
			}
		}

	}

	//================================================================================
	// Incoming Requests
	//================================================================================

	// Handoff
	public void incomingHandoff(URLRequest handoff) {
		
		
		// We've met our depth
		if (handoff.depth == depth) {
			return;
		}
		
		synchronized (crawlState) {
			System.out.println("Received handoff request for domain : " + handoff.domain);

			// If we're tracking this domain, handle the link
			if (crawlState.isTracking(handoff.domain)) {
				crawlState.addPage(new Page(handoff.url, handoff.depth, handoff.domain));

				handoff.addIntermediary(peer.hostname, peer.port);
				boolean leader = true;
				FetchParseTask fetcher = new FetchParseTask(handoff, this, leader);
				poolManager.execute(fetcher);
			}
		}
	}

	// Link to parse
	public void incomingUrlRequest(URLRequest request) {

		if (request.depth > depth) {
			System.out.println("uh oh! Depth is going too far: " + request.depth);
		}
		
		// fetch url
		System.out.println("Fetching : " + request.url + " : " + Tools.generateHash(request.url));
		boolean leader = false;
		FetchParseTask fetcher = new FetchParseTask(request, this, leader);
		poolManager.execute(fetcher);	

	}

	// Domain we own
	public void incomingDomainRequest(DomainRequest request) {
		synchronized (crawlState) {
			System.out.println("Received domain request : " + request.url);
			crawlState.addDomain(request.domain, request.requesterHostName, request.requesterPort);

			// Make ourselves the intermediary for any further requests on this domain
			request.addIntermediary(peer.hostname, peer.port);
			boolean leader = true;

			// Send it off to be fetched and processed
			FetchParseTask fetcher = new FetchParseTask(request, this, leader);
			poolManager.execute(fetcher);	
		}
	}


	// URL response
	public void incomingUrlResponse(URLResponse response) {
		
		if (crawlState.linkIsMine(response.domain)) {
			boolean domainLeader = true;
			handlePage(response, domainLeader);
			
			// If we're done
			if (!crawlState.shouldContinue()) {
				System.out.println("Completed crawl. Send back to requester");
			}
		}
		
		else {
			System.out.println("Recieved a URL Response for a domain that is not mine: " + response.domain);
		}
	}



	//================================================================================
	// Exit
	//================================================================================
	public void exit() {
		peer.leaveDHT();
	}

	//================================================================================
	//================================================================================
	// Main
	//================================================================================
	//================================================================================
	public static void main(String[] args){

		String discoveryHost = "";
		int discoveryPort = 0;
		int localPort = 0;
		int id = Constants.Undefined;
		int refreshTime = Constants.Refresh_Time;
		int depth = Constants.Crawl_Depth;

		if (args.length >= 3) {
			discoveryHost = args[0];
			discoveryPort = Integer.parseInt(args[1]);
			localPort = Integer.parseInt(args[2]);

			if (args.length >= 4){
				depth = Integer.parseInt(args[3]);

				if (args.length >= 5) {
					id = Integer.parseInt(args[4]);

					if (args.length >= 6) {
						refreshTime = Integer.parseInt(args[5]);

					}
				}
			}
		}

		else {
			System.out.println("Usage: java cs555.crawler.node.Crawler DISCOVERY-NODE DISCOVERY-PORT LOCAL-PORT DEPTH <HASH> <REFRESH-TIME>");
			System.exit(1);
		}


		Crawler crawler = new Crawler(depth, localPort, id, refreshTime);
		crawler.init(discoveryHost, discoveryPort);

		// Wait and accept User Commands
		boolean cont = true;
		while (cont){
			String input = Tools.readInput("Command: ");

			if (input.equalsIgnoreCase("exit")){
				crawler.exit();
				cont = false;
				System.exit(0);

			}
		}
	}

}