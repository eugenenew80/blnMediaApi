package kz.kegoc.bln.exp.ftp.sender;

import javax.ejb.Local;

@Local
public interface Sender<T> {
    void send();
}
