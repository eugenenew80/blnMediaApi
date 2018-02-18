package kz.kegoc.bln.exp;

import javax.ejb.Local;

@Local
public interface Exporter {
    void runExport();
}
