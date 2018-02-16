package kz.kegoc.bln.producer.emcos.reader;

import javax.ejb.Local;

@Local
public interface DataReader<T> {
    void read();
}
