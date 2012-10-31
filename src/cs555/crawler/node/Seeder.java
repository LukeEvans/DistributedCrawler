package cs555.crawler.node;

import cs555.crawler.communications.Link;
import cs555.crawler.peer.Peer;
import cs555.crawler.url.CrawlerState;
import cs555.crawler.url.Page;
import cs555.crawler.utilities.Constants;
import cs555.crawler.utilities.Tools;
import cs555.crawler.wireformats.*;
import cs555.crawler.wireformatsURL.DomainRequest;

public class Seeder extends Node {

	String hostName;
	CrawlerState crawlState;
	int port;

	public Seeder(int p, String fname, int fhash, CrawlerState c) {
		super(p);
		hostName = Tools.getLocalHostname();
		port = p;
		crawlState = c;

	}

	//================================================================================
	// Init
	//================================================================================
	public void initServer(){
		super.initServer();
	}

	public void initLookup(String dHost, int dPort) {
		Link managerLink = connect(new Peer(dHost, dPort));
		
		// Get random peer from discovery
		RandomPeerRequest randomReq = new RandomPeerRequest(hostName, port, Constants.Seed_Node);
		managerLink.sendData(randomReq.marshall());
		
		byte[] randomNodeData = managerLink.waitForData();
		int msgType = Tools.getMessageType(randomNodeData);
		
		switch (msgType) {
		case Constants.RandomPeer_Response:
			// We've gotten our random node
			RandomPeerResponse randomRes = new RandomPeerResponse();
			randomRes.unmarshall(randomNodeData);
			
			Peer accessPoint = new Peer(randomRes.hostName, randomRes.port, randomRes.id);
			Link accessLink = connect(accessPoint);
			
			// Publish each seed publish domain
			for (Page p : crawlState.getAllReadyLinks()) {
				DomainRequest domainReq = p.getDomainRequest(hostName, port);
				System.out.println("Link resolves to : " + domainReq.resolveID);
				
				accessLink.sendData(domainReq.marshall());
			}
			
			break;

		default:
			break;
		}
	}

	//================================================================================
	// Receive
	//================================================================================
	// Receieve data
	public synchronized void receive(byte[] bytes, Link l){
		int messageType = Tools.getMessageType(bytes);

		switch (messageType) {
		case Constants.Domain_Response:
			// Unmarshall response
			
			// Mark this domain complete
			
			// Print domain info
			
			break;

		default:
			System.out.println("Unrecognized Message");
			break;
		}
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
		String fileName = "";
		int depth = Constants.Crawl_Depth;

		if (args.length >= 4) {
			discoveryHost = args[0];
			discoveryPort = Integer.parseInt(args[1]);
			localPort = Integer.parseInt(args[2]);
			fileName = args[3];
			
			if (args.length >= 5) {
				depth = Integer.parseInt(args[4]);
			}
		}

		else {
			System.out.println("Usage: java cs555.crawler.node.Seeder DISCOVERY-NODE DISCOVERY-PORT LOCAL-PORT FILE-NAME <CRAWL_DEPTH>");
			System.exit(1);
		}

		// Create crawl state
		CrawlerState seeds = new CrawlerState(fileName, depth);
		
		// Create node
		Seeder storeHandler = new Seeder(localPort, fileName, Constants.Undefined, seeds);

		// Start
		storeHandler.initServer();
		storeHandler.initLookup(discoveryHost, discoveryPort);
	}
}
