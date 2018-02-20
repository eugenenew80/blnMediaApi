package kz.kegoc.bln.imp.emcos.reader.auto;

import kz.kegoc.bln.imp.emcos.reader.Reader;
import javax.ejb.Local;

@Local
public interface AutoReader<T> extends Reader<T> {
    void read();
}
