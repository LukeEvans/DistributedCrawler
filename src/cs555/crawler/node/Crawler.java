package cs555.crawler.node;

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

	//================================================================================
	// Init
	//================================================================================
	public Crawler(int d, int p, int i, int r) {
		depth = d;
		peer = new PeerNode(p, i, r, this);
	}

	public void init(String d, int p){
		peer.initServer();
		peer.enterDHT(d, p);
	}



	//================================================================================
	// Publish
	//================================================================================
	public void publishLink(Page page) {
		peer.publishLink(page.domain, page.urlString, page.depth);
	}

	//================================================================================
	// Incoming Requests
	//================================================================================
	// Link to parse
	public synchronized void incomingUrlRequest(URLRequest request) {
		// fetch url
		
	}

	// Domain we own
	public synchronized void incomingDomainRequest(DomainRequest request) {
		
	}
	
	// URL response
	public synchronized void incomingUrlResponse(URLResponse response) {
		
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