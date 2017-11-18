package kz.kegoc.bln.gateway.emcos;

import javax.ejb.Local;
import java.time.LocalDateTime;
import java.util.List;

@Local
public interface EmcosFlowGateway {
    EmcosFlowGateway cfg(List<EmcosPointCfg> pointsCfg);

    EmcosFlowGateway requestedTime(LocalDateTime requestedTime);

    EmcosFlowGateway paramCode(String paramCode);

    List<MinuteMeteringFlow> request();
}
