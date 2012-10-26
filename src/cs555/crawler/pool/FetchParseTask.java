package cs555.crawler.pool;

import org.htmlparser.beans.*;

import cs555.crawler.communications.*;

public class FetchParseTask implements Task {

	Link link;
	int runningThread;
	
	// Fetchers
	HTMLTextBean textFetcher;
	HTMLLinkBean linkfetcher;
	
	// URL
	String urlString;
	
	// Text
	String urlText;
	
	
	
	//================================================================================
	// Constructor
	//================================================================================
	public FetchParseTask(Link l, String url){
		link = l;
		textFetcher = new HTMLTextBean();
		linkfetcher = new HTMLLinkBean();
		urlString = url;
		
		textFetcher.setURL(url);
		linkfetcher.setURL(url);
	}
	
	//================================================================================
	// Run
	//================================================================================
	public void run() {
		//URL[] links = linkfetcher.getLinks();
		
	}

	
	
	//================================================================================
	// House Keeping
	//================================================================================
	public void setRunning(int i) {
		runningThread = i;
	}

}
