package kz.kegoc.bln.producer.file.reader;

import kz.kegoc.bln.entity.media.Metering;
import java.nio.file.Path;

public interface FileMeteringReader<T extends Metering> {
    void read(Path path);
}
