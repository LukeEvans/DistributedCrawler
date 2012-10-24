package cs555.crawler.node;

import cs555.crawler.communications.Link;
import cs555.crawler.peer.Peer;
import cs555.crawler.peer.PeerList;
import cs555.crawler.utilities.Constants;
import cs555.crawler.utilities.Tools;
import cs555.crawler.wireformats.DeregisterRequest;
import cs555.crawler.wireformats.Payload;
import cs555.crawler.wireformats.RandomPeerResponse;
import cs555.crawler.wireformats.RegisterRequest;
import cs555.crawler.wireformats.RegisterResponse;
import cs555.crawler.wireformats.Verification;

public class DiscoveryNode extends Node{

	PeerList peerList;

	//================================================================================
	// Constructor
	//================================================================================
	public DiscoveryNode(PeerList list, int port){
		super(port);

		peerList = list;
	}



	//================================================================================
	// Receive
	//================================================================================
	// Receieve data
	public synchronized void receive(byte[] bytes, Link l){
		int messageType = Tools.getMessageType(bytes);

		switch (messageType) {

		case Constants.Registration_Request:

			RegisterRequest rreq = new RegisterRequest();
			rreq.unmarshall(bytes);

			// If peer's id collides, return a failure message
			if (!peerList.hashUnique(rreq.id)) {
				Verification failure = new Verification(Constants.Failure);
				l.sendData(failure.marshall());
				break;
			}

			// Send a success
			Verification success = new Verification(Constants.Success);
			l.sendData(success.marshall());
			l.waitForIntReply();

			// Return a random peer
			Peer returnPeer = peerList.getNextPeer();

			// If return peer is null, the requesting node is the first to join. Return null
			if (returnPeer == null) {
				Payload nullPeer = new Payload(Constants.Null_Peer);
				l.sendData(nullPeer.marshall());
			}

			else {
				// Else, we have a valid peer to return
				RegisterResponse rresp = new RegisterResponse(returnPeer.hostname, returnPeer.port, returnPeer.id);
				l.sendData(rresp.marshall());

				System.out.println("returning node : " + rresp.id);
			}
			
			// Add peer to list
			Peer addPeer = new Peer(rreq.hostName, rreq.port, rreq.id);
			peerList.addPeer(addPeer);

			System.out.println(rreq.id + " joined the system\n");
			
			break;

		case Constants.Deregister_Request:
			DeregisterRequest deregisterRequest = new DeregisterRequest();
			deregisterRequest.unmarshall(bytes);

			// Remove peer
			Peer removePeer = new Peer(deregisterRequest.hostName, deregisterRequest.port, deregisterRequest.id);
			peerList.removePeer(removePeer);

			System.out.println(removePeer.id + " has left the system");
			break;

		case Constants.RandomPeer_Requst:
			
			Peer poc = peerList.getNextPeer();
			RandomPeerResponse randomPeer = new RandomPeerResponse(poc.hostname, poc.port, poc.id);
			l.sendData(randomPeer.marshall());
			
			break;
			
		default:
			System.out.println("Unrecognized Message " + messageType);
			break;
		}
	}


	//================================================================================
	//================================================================================
	// Main
	//================================================================================
	//================================================================================
	public static void main(String[] args){

		int port = 0;

		if (args.length == 1) {
			port = Integer.parseInt(args[0]);

		}

		else {
			System.out.println("Usage: java cs555.dht.node.DiscoveryNode PORT");
			System.exit(1);
		}

		// Create peer list
		PeerList peerList = new PeerList();

		// Create node
		DiscoveryNode manager = new  DiscoveryNode(peerList, port);
		manager.initServer();

	}
}
