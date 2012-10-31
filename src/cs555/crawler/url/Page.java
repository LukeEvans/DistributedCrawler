package cs555.crawler.url;

import cs555.crawler.wireformatsURL.*;
import cs555.crawler.peer.Peer;
import cs555.crawler.utilities.*;

public class Page {

	public int status;
	public String urlString;
	public int depth;
	public String domain;
	public int urlHash;
	public int domainHash;
	PageMetadata metadata;
	
	public Peer requester;
	
	//================================================================================
	// Constructor
	//================================================================================
	public Page(String url, String d){
		urlString = d;
		domain = urlString;
		status = Constants.URL_Ready;
		depth = 0;
		urlHash = Tools.generateHash(urlString);
		domainHash = Tools.generateHash(domain);
		metadata = new PageMetadata();
		
	}
	
	public Page(String url, int dep, String d){
		urlString = url;
		domain = d;
		status = Constants.URL_Ready;
		depth = dep;
		urlHash = Tools.generateHash(urlString);
		domainHash = Tools.generateHash(domain);
		metadata = new PageMetadata();
	}
	
	//================================================================================
	// Modifiers
	//================================================================================
	public void parseResponse(URLResponse resp) {
		metadata.parseResponse(resp);
	}
	
	public void addRequester(String host, int p) {
		requester = new Peer(host, p);
	}
	//================================================================================
	// Get requests for page
	//================================================================================
	public DomainRequest getDomainRequest(String host, int port) {
		return new DomainRequest(host, port, Constants.Seed_Node, domainHash, domain, urlString);
	}
	
	//================================================================================
	// House Keeping
	//================================================================================
	// Override .equals
	public boolean equals(Page other){
		if (this.urlString.equalsIgnoreCase(other.urlString)){
			return true;
		}
		return false;
	}
	
	// Override toString
	public String toString(){
		String s = "";
		
		s += urlString + " " + status;
		
		return s;
	}
}
