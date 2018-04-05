package kz.kegoc.bln.gateway;

import javax.ejb.Local;

@Local
public interface CompressService {
    byte[] compress(byte[] data) throws Exception;
    byte[] decompress(byte[] data) throws Exception;
}
