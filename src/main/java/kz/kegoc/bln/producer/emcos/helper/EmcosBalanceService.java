package kz.kegoc.bln.producer.emcos.helper;

import kz.kegoc.bln.entity.media.day.DayMeteringBalanceRaw;

import java.time.LocalDateTime;
import java.util.List;

public interface EmcosBalanceService {
    EmcosBalanceService cfg(List<EmcosPointCfg> pointsCfg);

    EmcosBalanceService requestedTime(LocalDateTime requestedTime);

    EmcosBalanceService paramCode(String paramCode);

    List<DayMeteringBalanceRaw> request();
}
