package cs555.crawler.url;

import java.util.ArrayList;

import cs555.crawler.wireformatsURL.*;
import cs555.crawler.utilities.*;

public class Page {

	public int status;
	public String urlString;
	public int depth;
	public String domain;
	public int urlHash;
	public int domainHash;
	ArrayList<Page> links;
	
	//================================================================================
	// Constructor
	//================================================================================
	public Page(String url, String d){
		urlString = d;
		domain = urlString;
		status = Constants.URL_Ready;
		depth = 0;
		links = new ArrayList<Page>();
		urlHash = Tools.generateHash(urlString);
		domainHash = Tools.generateHash(domain);
		
	}
	
	public Page(String url, int dep, String d){
		urlString = url;
		domain = d;
		status = Constants.URL_Ready;
		depth = dep;
		links = new ArrayList<Page>();
		urlHash = Tools.generateHash(urlString);
		domainHash = Tools.generateHash(domain);
	}
	
	//================================================================================
	// Modifiers
	//================================================================================
	public ArrayList<String> stringLinks(){
		ArrayList<String> stringLinks = new ArrayList<String>();
		
		for (Page p : links){
			stringLinks.add(p.urlString);
		}
		
		return stringLinks;
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
