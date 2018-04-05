package kz.kegoc.bln.gateway.emcos;

import javax.ejb.Local;

@Local
public interface HttpGateway {
    byte[] doRequest() throws Exception;
}
