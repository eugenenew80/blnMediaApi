package kz.kegoc.bln.producer.emcos.reader;

import kz.kegoc.bln.entity.media.MeteringData;

public interface EmcosMeteringDataReader<T extends MeteringData> {
    void loadFromEmcos();
}
