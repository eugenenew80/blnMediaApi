package kz.kegoc.bln.producer.emcos.helper;

import java.time.LocalDateTime;
import java.util.List;

public interface EmcosDataService {
    void setPointsCfg(List<EmcosPointCfg> pointsCfg);
    List<MinuteMeteringDataDto> request(String paramCode, LocalDateTime requestedDateTime);
}
