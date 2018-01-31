package kz.kegoc.bln.gateway.emcos;

import kz.kegoc.bln.entity.data.MeasDataRaw;

import javax.ejb.Local;
import java.time.LocalDateTime;
import java.util.List;

@Local
public interface MeasDataRawGateway {
    MeasDataRawGateway cfg(List<MeteringPointCfg> pointsCfg);

    MeasDataRawGateway requestedTime(LocalDateTime requestedTime);

    MeasDataRawGateway paramCode(String paramCode);

    List<MeasDataRaw> request();
}
