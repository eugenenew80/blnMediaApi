package kz.kegoc.bln.gateway.emcos;

import kz.kegoc.bln.entity.data.MeteringReadingRaw;
import javax.ejb.Local;
import java.time.LocalDateTime;
import java.util.List;

@Local
public interface MeteringReadingRawGateway {
    MeteringReadingRawGateway cfg(List<MeteringPointCfg> pointsCfg);

    MeteringReadingRawGateway requestedTime(LocalDateTime requestedTime);

    MeteringReadingRawGateway paramCode(String paramCode);

    List<MeteringReadingRaw> request();
}
