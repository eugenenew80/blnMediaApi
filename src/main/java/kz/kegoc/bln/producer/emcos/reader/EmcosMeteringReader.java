package kz.kegoc.bln.producer.emcos.reader;

import kz.kegoc.bln.entity.media.Metering;

public interface EmcosMeteringReader<T extends Metering> {
    void read();
}
