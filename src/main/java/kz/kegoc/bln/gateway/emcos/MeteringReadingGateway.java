package kz.kegoc.bln.gateway.emcos;

import kz.kegoc.bln.entity.data.EmcosConfig;
import kz.kegoc.bln.entity.data.MeteringReadingRaw;
import kz.kegoc.bln.entity.data.Parameter;
import javax.ejb.Local;
import java.util.List;

@Local
public interface MeteringReadingGateway {
    MeteringReadingGateway config(EmcosConfig config);

    MeteringReadingGateway points(List<MeteringPointCfg> points);

    List<MeteringReadingRaw> request();
}
