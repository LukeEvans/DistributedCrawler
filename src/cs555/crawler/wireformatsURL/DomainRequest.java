package cs555.crawler.wireformatsURL;

import cs555.crawler.utilities.Constants;

public class DomainRequest extends URLRequest{

	//================================================================================
	// Overridden Constructors
	//================================================================================
	public DomainRequest(String h, int p, int i, int r, String d, String u){
		super.init(h, p, i, r, d, u);
		type = Constants.Domain_Request;
		
	}
	
	public DomainRequest(){
		super.init("", 0, -1,-1, "", "");
		type = Constants.Domain_Request;
	}
	
}
