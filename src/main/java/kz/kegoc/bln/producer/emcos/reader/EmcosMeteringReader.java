package kz.kegoc.bln.producer.emcos.reader;

import kz.kegoc.bln.entity.media.Metering;
import javax.ejb.Local;

@Local
public interface EmcosMeteringReader<T extends Metering> {
    void read();
}
