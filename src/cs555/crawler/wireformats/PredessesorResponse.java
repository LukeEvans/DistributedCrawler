package cs555.crawler.wireformats;

import cs555.crawler.utilities.Constants;

public class PredessesorResponse extends RegisterRequest{

	//================================================================================
	// Overridden Constructors
	//================================================================================
	public PredessesorResponse(String h, int p, int i){
		super.init(h, p, i);
		type = Constants.Predesessor_Response;
		
	}
	
	
	public PredessesorResponse(){
		super.init("", 0, -1);
		type = Constants.Predesessor_Response;
	}
}
