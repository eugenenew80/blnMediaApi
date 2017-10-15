package kz.kegoc.bln.producer.file;

import kz.kegoc.bln.entity.media.MeteringData;
import java.nio.file.Path;

public interface FileMeteringDataRawReader<T extends MeteringData> {
    void loadFromFile(Path path) throws Exception;
}
