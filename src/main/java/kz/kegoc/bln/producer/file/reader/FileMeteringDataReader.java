package kz.kegoc.bln.producer.file.reader;

import kz.kegoc.bln.entity.common.Metering;

import javax.ejb.Local;
import java.nio.file.Path;

@Local
public interface FileMeteringDataReader<T extends Metering> {
    void read(Path path);
}
