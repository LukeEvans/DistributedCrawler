package cs555.crawler.wireformats;

import cs555.crawler.utilities.Constants;

public class RandomPeerRequest extends RegisterRequest{

	//================================================================================
	// Overridden Constructors
	//================================================================================
	public RandomPeerRequest(String h, int p, int i){
		super.init(h, p, i);
		type = Constants.RandomPeer_Requst;
		
	}
	
	
	public RandomPeerRequest(){
		super.init("", 0, -1);
		type = Constants.RandomPeer_Requst;
	}
}
