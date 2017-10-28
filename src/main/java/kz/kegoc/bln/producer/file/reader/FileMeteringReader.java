package kz.kegoc.bln.producer.file.reader;

import kz.kegoc.bln.entity.media.Metering;

import javax.ejb.Local;
import java.nio.file.Path;

@Local
public interface FileMeteringReader<T extends Metering> {
    void read(Path path);
}
