package kz.kegoc.bln.gateway.emcos;

import kz.kegoc.bln.entity.data.ConnectionConfig;
import kz.kegoc.bln.entity.data.MeteringReadingRaw;

import javax.ejb.Local;
import java.util.List;

@Local
public interface MeteringReadingGateway {
    MeteringReadingGateway config(ConnectionConfig config);

    MeteringReadingGateway points(List<MeteringPointCfg> points);

    List<MeteringReadingRaw> request() throws Exception;
}
