package cs555.crawler.pool;

import java.net.URL;

import org.htmlparser.beans.*;

public class FetchParseTask implements Task {

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
	public FetchParseTask(String url){
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
		System.out.println("fetching : " +urlString);
		URL[] links = linkfetcher.getLinks();
		
		System.out.println("length : " + links.length);
		
		for (URL u : links) {
			System.out.println("Link : " + u.toString());
		}
		
		return;
	}

	
	
	//================================================================================
	// House Keeping
	//================================================================================
	public void setRunning(int i) {
		runningThread = i;
	}

}
