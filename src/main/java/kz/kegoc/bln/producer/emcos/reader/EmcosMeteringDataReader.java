package kz.kegoc.bln.producer.emcos.reader;

import kz.kegoc.bln.entity.common.Metering;
import javax.ejb.Local;

@Local
public interface EmcosMeteringDataReader<T extends Metering> {
    void read();
}
