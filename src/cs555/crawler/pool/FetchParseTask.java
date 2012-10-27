package cs555.crawler.pool;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.htmlparser.beans.*;

import cs555.crawler.wireformatsURL.URLRequest;

public class FetchParseTask implements Task {

	int runningThread;
	
	// Fetchers
	HTMLTextBean textfetcher;
	LinkBean linkfetcher;
	
	// URL
	String urlString;
	
	// Text
	String urlText;
	
	// Request 
	URLRequest request;
	
	//================================================================================
	// Constructor
	//================================================================================
	public FetchParseTask(URLRequest urlReq){
		textfetcher = new HTMLTextBean();
		linkfetcher = new LinkBean();
		urlString = urlReq.url;
		request = urlReq;
		
		textfetcher.setURL(urlString);
		linkfetcher.setURL(urlString);
	}
	
	//================================================================================
	// Run
	//================================================================================
	public void run() {

		try {
			URL url = new URL(urlString);
			URLConnection urlConnection;
			urlConnection = url.openConnection();
			
			linkfetcher.setConnection(urlConnection);
			textfetcher.setConnection(urlConnection);
			
			URL [] urls = linkfetcher.getLinks();
			
			ArrayList<URL> domainSpecific = new ArrayList<URL>();
			
			// For the full list of urls, determine which we care about
			for (URL link : urls) {
				
				// If the link belongs to the domain, add it
				if (link.toString().startsWith(request.domain)) {
					domainSpecific.add(link);
				}
			}
			

			for (URL link : domainSpecific) {
				System.out.println(link.toString());
			}
			
			System.out.println("Length : " + domainSpecific.size());
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
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
