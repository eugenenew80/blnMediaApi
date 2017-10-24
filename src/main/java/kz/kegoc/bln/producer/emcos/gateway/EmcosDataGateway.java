package kz.kegoc.bln.producer.emcos.gateway;

import java.time.LocalDateTime;
import java.util.List;

public interface EmcosDataGateway {
    EmcosDataGateway cfg(List<EmcosPointCfg> pointsCfg);

    EmcosDataGateway requestedTime(LocalDateTime requestedTime);

    EmcosDataGateway paramCode(String paramCode);

    List<MinuteMeteringData> request();
}
