package cs555.crawler.pool;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.htmlparser.beans.*;

import cs555.crawler.node.Crawler;
import cs555.crawler.wireformatsURL.URLRequest;
import cs555.crawler.wireformatsURL.URLResponse;

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
			// URL
			URL url = new URL(urlString);
			
			// URL connections
			URLConnection linkURLConnection;
			linkURLConnection = url.openConnection();

			URLConnection textURLConnection;
			textURLConnection = url.openConnection();
			
			linkfetcher.setConnection(linkURLConnection);
			textfetcher.setConnection(textURLConnection);

			URL [] urls = linkfetcher.getLinks();
			String webString = textfetcher.getText();	
			
			URLResponse response = new URLResponse(request, stringURLs(urls), getFileString(urls));
			
			crawler.handlePage(response, domainLeader);

			// Save webString to file in basePath + /url
			System.out.println(webString);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		return;
	}


	//================================================================================
	// Parse
	//================================================================================
	public String getFileString(URL[] links) {
		int html = 0;
		int htm = 0;
		int doc = 0;
		int pdf = 0;
		int cfm = 0;
		int aspx = 0;
		int asp = 0;
		int php = 0;
		
		for (URL u : links) {
			String s = u.toString();
			
			if (s.endsWith(".html")) html++;
			else if (s.endsWith(".htm")) htm++;
			else if (s.endsWith(".doc")) doc++;
			else if (s.endsWith(".pdf")) pdf++;
			else if (s.endsWith(".cfm")) cfm++;
			else if (s.endsWith(".aspx")) aspx++;
			else if (s.endsWith(".asp")) asp++;
			else if (s.endsWith(".php")) php++;
		}
		
		String s = "html=" + html;
		s += ";htm=" + htm;
		s += ";doc=" + doc;
		s += ";pdf=" + pdf;
		s += ";cfm=" + cfm;
		s += ";aspx=" + aspx;
		s += ";asp=" + asp;
		s += ";php=" + php;
		
		return s;
	}

	public ArrayList<String> stringURLs(URL[] urls) {
		ArrayList<String> strings = new ArrayList<String>();
		
		for (URL u : urls) {
			strings.add(u.toString());
		}
		
		return strings;
	}
	//================================================================================
	// House Keeping
	//================================================================================
	public void setRunning(int i) {
		runningThread = i;
	}

}
