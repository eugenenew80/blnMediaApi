package kz.kegoc.bln.producer.emcos.helper;

import java.time.LocalDateTime;
import java.util.List;

public interface EmcosDataService {
    List<MinuteMeteringDataDto> request(String paramCode, LocalDateTime requestedDateTime);
}
