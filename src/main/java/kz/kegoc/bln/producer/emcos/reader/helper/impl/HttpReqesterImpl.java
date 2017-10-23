package kz.kegoc.bln.producer.emcos.reader.helper.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kz.kegoc.bln.producer.emcos.reader.helper.HttpRequester;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpReqesterImpl implements HttpRequester {
	private static Logger logger = LoggerFactory.getLogger(HttpReqesterImpl.class);

	private final URL url;
	private final String method;
	private final String body;
	
	private HttpReqesterImpl(Builder builder) {
		this.url = builder.url;
		this.method = builder.method;
		this.body = builder.body;
	}
	
    public String doRequest() throws IOException {
		logger.info("http request started...");
		logger.info("url: " + url);
		logger.info("method: " + method);

		StringBuffer response = new StringBuffer();
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod(method);
			con.setDoOutput(true);

			try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
				wr.writeBytes(body);
				wr.flush();
			}

			try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
				String output;
				while ((output = in.readLine()) != null) {
					response.append(output);
				}
			}
			logger.info("http request completed");
		}

		catch (IOException e) {
			logger.info("http request failed: " + e);
			throw e;
		}

		finally {
			if (con!=null) con.disconnect();
		}

		String rawData = response.toString();

		int n1 = rawData.indexOf("<AnswerData>");
		int n2 = rawData.indexOf("</AnswerData>");

		String answerData = "";
		if (n2>n1)
			answerData = rawData.substring(n1+12, n2);

        return answerData;
	}
    
    
    public static class Builder {
    	private URL url;
    	private String method;
    	private String body;
    	
    	public Builder url(final URL url) {
    		this.url = url;
    		return this;
    	}
    	    	
    	public Builder method(final String method) {
    		this.method = method;
    		return this;
    	}    	

    	public Builder body(final String body) {
    		this.body = body;
    		return this;
    	}        
    	
    	public HttpRequester build() {
    		return new HttpReqesterImpl(this); 
    	}
    }
}
