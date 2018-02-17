package kz.kegoc.bln.imp.emcos.reader;

import javax.ejb.Local;

@Local
public interface Reader<T> {
    void read();
}
