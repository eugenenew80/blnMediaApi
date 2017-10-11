package kz.kegoc.bln.producer.emcos;

import java.net.URL;

public interface HttpRequest {
    String doRequest(URL url, String requestBody) throws Exception;
}
