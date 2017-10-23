package kz.kegoc.bln.producer.emcos.reader.helper;

import java.time.LocalDateTime;
import java.util.List;

public interface EmcosDataService {
    List<MinuteMeteringDataDto> request(String paramCode, LocalDateTime requestedDateTime);
}
