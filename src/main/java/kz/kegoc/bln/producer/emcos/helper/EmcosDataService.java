package kz.kegoc.bln.producer.emcos.helper;

import java.time.LocalDateTime;
import java.util.List;

public interface EmcosDataService {
    EmcosDataService cfg(List<EmcosPointCfg> pointsCfg);

    EmcosDataService requestedTime(LocalDateTime requestedTime);

    EmcosDataService paramCode(String paramCode);

    List<MinuteMeteringDataDto> request();
}
