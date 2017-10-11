package kz.kegoc.bln.producer.emcos;

import java.net.URL;

public interface HttpRequester {
    String doRequest(URL url, String requestBody) throws Exception;
}
