package cs555.crawler.wireformats;

import cs555.crawler.utilities.Constants;

public class RegisterResponse extends RegisterRequest{

	//================================================================================
	// Overridden Constructors
	//================================================================================
	public RegisterResponse(String h, int p, int i){
		super.init(h, p, i);
		type = Constants.Registration_Reply;
		
	}
	
	
	public RegisterResponse(){
		super.init("", 0, -1);
		type = Constants.Registration_Reply;
	}
}
