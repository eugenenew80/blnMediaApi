package kz.kegoc.bln.exp.emcos.sender;

import javax.ejb.Local;

@Local
public interface Sender<T> {
    void send();
}
