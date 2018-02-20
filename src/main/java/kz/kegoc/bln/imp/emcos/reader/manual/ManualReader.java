package kz.kegoc.bln.imp.emcos.reader.manual;

import kz.kegoc.bln.imp.emcos.reader.Reader;

import javax.ejb.Local;

@Local
public interface ManualReader<T> extends Reader<T> {
    void read();
}
