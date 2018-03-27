package kz.kegoc.bln.imp;

import javax.ejb.Local;

@Local
public interface Reader<T>  {
    void read();
}
