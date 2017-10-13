package kz.kegoc.bln.producer.emcos.impl;

import kz.kegoc.bln.producer.emcos.HttpRequester;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpReqesterImpl implements HttpRequester {
	private final URL url;
	private final String method;
	private final String body;
	
	private HttpReqesterImpl(Builder builder) {
		this.url = builder.url;
		this.method = builder.method;
		this.body = builder.body;
	}
	
    public String doRequest() throws Exception {
    	HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);
        con.setDoOutput(true);

        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(body);
            wr.flush();
        }

        StringBuffer response = new StringBuffer();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String output;
            while ((output = in.readLine()) != null)
                response.append(output);
        }

        String rawData = response.toString();

        int n1 = rawData.indexOf("<AnswerData>");
        int n2 = rawData.indexOf("</AnswerData>");

        if (n2>n1)
            return rawData.substring(n1+12, n2);
        else
            return "";
    }
    
    
    public static class Builder {
    	private URL url;
    	private String method;
    	private String body;
    	
    	public Builder url(URL url) {
    		this.url = url;
    		return this;
    	}
    	    	
    	public Builder method(String method) {
    		this.method = method;
    		return this;
    	}    	

    	public Builder body(String body) {
    		this.body = body;
    		return this;
    	}        
    	
    	public HttpRequester build() {
    		return new HttpReqesterImpl(this); 
    	}
    }
}
