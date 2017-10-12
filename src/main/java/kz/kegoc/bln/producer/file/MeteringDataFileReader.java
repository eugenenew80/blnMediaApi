package kz.kegoc.bln.producer.file;

import kz.kegoc.bln.entity.common.HasId;
import java.nio.file.Path;
import java.util.List;

public interface MeteringDataFileReader<T extends HasId> {
    void loadFromFile(Path path) throws Exception;
}
