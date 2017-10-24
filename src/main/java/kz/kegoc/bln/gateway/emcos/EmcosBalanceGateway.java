package kz.kegoc.bln.gateway.emcos;

import kz.kegoc.bln.entity.media.day.DayMeteringBalanceRaw;

import java.time.LocalDateTime;
import java.util.List;

public interface EmcosBalanceGateway {
    EmcosBalanceGateway cfg(List<EmcosPointCfg> pointsCfg);

    EmcosBalanceGateway requestedTime(LocalDateTime requestedTime);

    EmcosBalanceGateway paramCode(String paramCode);

    List<DayMeteringBalanceRaw> request();
}
