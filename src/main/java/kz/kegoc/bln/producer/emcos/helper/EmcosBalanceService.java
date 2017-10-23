package kz.kegoc.bln.producer.emcos.helper;

import kz.kegoc.bln.entity.media.day.DayMeteringBalanceRaw;

import java.time.LocalDateTime;
import java.util.List;

public interface EmcosBalanceService {
    void setPointsCfg(List<EmcosPointCfg> pointsCfg);
    List<DayMeteringBalanceRaw> request(String paramCode, LocalDateTime requestedDateTime);
}
