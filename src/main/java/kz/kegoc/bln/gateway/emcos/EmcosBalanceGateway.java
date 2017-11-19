package kz.kegoc.bln.gateway.emcos;

import kz.kegoc.bln.entity.media.raw.DayMeteringBalance;

import javax.ejb.Local;
import java.time.LocalDateTime;
import java.util.List;

@Local
public interface EmcosBalanceGateway {
    EmcosBalanceGateway cfg(List<EmcosPointCfg> pointsCfg);

    EmcosBalanceGateway requestedTime(LocalDateTime requestedTime);

    EmcosBalanceGateway paramCode(String paramCode);

    List<DayMeteringBalance> request();
}
