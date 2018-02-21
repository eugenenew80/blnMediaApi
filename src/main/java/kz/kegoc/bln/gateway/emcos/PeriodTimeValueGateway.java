package kz.kegoc.bln.gateway.emcos;

import kz.kegoc.bln.entity.data.ConnectionConfig;
import kz.kegoc.bln.entity.data.PeriodTimeValueRaw;

import javax.ejb.Local;
import java.util.List;

@Local
public interface PeriodTimeValueGateway {
    PeriodTimeValueGateway config(ConnectionConfig config);

    PeriodTimeValueGateway points(List<MeteringPointCfg> points);

    List<PeriodTimeValueRaw> request() throws Exception;
}