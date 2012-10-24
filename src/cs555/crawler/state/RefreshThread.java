package cs555.crawler.state;

import cs555.crawler.node.PeerNode;
import cs555.crawler.utilities.Tools;

public class RefreshThread extends Thread {

	PeerNode peer;
	int refreshTime;
	public boolean cont;

	//================================================================================
	// Constructor
	//================================================================================
	public RefreshThread(PeerNode p, int r) {
		peer = p;
		refreshTime = r;
		cont = true;
	}

	//================================================================================
	// Run
	//================================================================================
	public void run() {

		while (cont) {
			// Sleep
			Tools.sleep(refreshTime);

			// Update
			peer.updateFT();
		}
	}
}
