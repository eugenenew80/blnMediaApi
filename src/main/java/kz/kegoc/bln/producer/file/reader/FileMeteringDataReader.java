package kz.kegoc.bln.producer.file.reader;

import kz.kegoc.bln.entity.media.MeteringData;
import java.nio.file.Path;

public interface FileMeteringDataReader<T extends MeteringData> {
    void loadFromFile(Path path) throws Exception;
}
