package cs555.crawler.wireformats;

import cs555.crawler.utilities.Constants;

public class SuccessorRequest extends RegisterRequest{

	//================================================================================
	// Overridden Constructors
	//================================================================================
	public SuccessorRequest(String h, int p, int i){
		super.init(h, p, i);
		type = Constants.Successor_Request;
		
	}
	
	
	public SuccessorRequest(){
		super.init("", 0, -1);
		type = Constants.Successor_Request;
	}
}
