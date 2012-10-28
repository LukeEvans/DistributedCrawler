package cs555.crawler.pool;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

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
			crawler.handlePage(request, urls, domainLeader);


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
