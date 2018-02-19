package kz.kegoc.bln.imp.emcos.auto;

import javax.ejb.Local;

@Local
public interface Reader<T> {
    void read();
}
