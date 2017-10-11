package kz.kegoc.bln.producer.emcos;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpReqesterImpl implements HttpRequester {
    public String doRequest(URL url, String requestBody) throws Exception {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);

        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(requestBody);
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
}
