package cs555.crawler.wireformatsURL;

import cs555.crawler.utilities.Constants;

public class HandoffRequest extends URLRequest{

	//================================================================================
	// Overridden Constructors
	//================================================================================
	public HandoffRequest(String h, int p, int i, int r, String d, String u){
		super.init(h, p, h, p, i, r, d, u, 0);
		type = Constants.Handoff_Request;
		
	}
	
	public HandoffRequest(){
		super.init("", 0, "", 0, -1,-1, "", "", 0);
		type = Constants.Handoff_Request;
	}
	
}
