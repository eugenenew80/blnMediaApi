package kz.kegoc.bln.gateway.emcos;

import javax.ejb.Local;
import java.time.LocalDateTime;
import java.util.List;

@Local
public interface EmcosDataGateway {
    EmcosDataGateway cfg(List<EmcosPointCfg> pointsCfg);

    EmcosDataGateway requestedTime(LocalDateTime requestedTime);

    EmcosDataGateway paramCode(String paramCode);

    List<MinuteMeteringData> request();
}
