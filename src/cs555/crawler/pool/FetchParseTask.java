package cs555.crawler.pool;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.htmlparser.beans.*;

import cs555.crawler.node.Crawler;
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

	// Crawler to pass back to
	Crawler crawler;

	// Domain handler
	boolean domainLeader;

	//================================================================================
	// Constructor
	//================================================================================
	public FetchParseTask(URLRequest urlReq, Crawler c, boolean leader){
		textfetcher = new HTMLTextBean();
		linkfetcher = new LinkBean();
		urlString = urlReq.url;
		request = urlReq;

		textfetcher.setURL(urlString);
		linkfetcher.setURL(urlString);

		crawler = c;
		domainLeader = leader;
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
					
					// If we're in charge of fowarding, publish
					if (domainLeader) {
						URLRequest req = request.getNextLevelRequest(link.toString());
						crawler.publishLink(req);
					}
				}
			}
			
			// If we're not the leader, send back
			if (!domainLeader) {
				System.out.println("Sending back: ");
				for (URL u : domainSpecific) {
					System.out.println(u);
				}
			}


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
