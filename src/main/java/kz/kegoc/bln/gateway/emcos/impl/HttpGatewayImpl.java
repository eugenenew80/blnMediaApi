package kz.kegoc.bln.gateway.emcos.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kz.kegoc.bln.gateway.emcos.HttpGateway;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpGatewayImpl implements HttpGateway {
	private static Logger logger = LoggerFactory.getLogger(HttpGatewayImpl.class);
	private final URL url;
	private final String method;
	private final String body;
	
	private HttpGatewayImpl(Builder builder) {
		this.url = builder.url;
		this.method = builder.method;
		this.body = builder.body;
	}
	
    public String doRequest() throws IOException {
		logger.info("HttpGatewayImpl.doRequest started");
		logger.info("url: " + url);
		logger.info("method: " + method);

		StringBuffer response = new StringBuffer();
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod(method);
			con.setUseCaches(false);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setConnectTimeout(99999999);
			con.setReadTimeout(99999999);

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
			logger.info("HttpGatewayImpl.doRequest successfully completed");
		}

		catch (IOException e) {
			logger.error("HttpGatewayImpl.doRequest failed: " + e);
			throw e;
		}

		finally {
			if (con!=null) con.disconnect();
		}

		String rawData = response.toString();

		String answerData;
		int n1 = rawData.indexOf("<AnswerData>");
		int n2 = rawData.indexOf("</AnswerData>");
		if (n2>n1)
			answerData = rawData.substring(n1+12, n2);
		else {
			n1 = rawData.indexOf("<?xml version=\"1.0\"?>");
			n2 = rawData.indexOf("</SOAP-ENV:Envelope>");
			if (n2>n1)
				answerData = rawData.substring(n1, n2 + 20);
			else
				answerData = rawData;
		}

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
    	
    	public HttpGateway build() {
    		return new HttpGatewayImpl(this); 
    	}
    }
}
