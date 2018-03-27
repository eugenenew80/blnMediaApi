package kz.kegoc.bln.gateway.emcos;

import kz.kegoc.bln.entity.data.ConnectionConfig;
import kz.kegoc.bln.imp.raw.PeriodTimeValueRaw;

import javax.ejb.Local;
import java.util.List;

@Local
public interface PeriodTimeValueImpGateway {
    PeriodTimeValueImpGateway config(ConnectionConfig config);

    PeriodTimeValueImpGateway points(List<MeteringPointCfg> points);

    List<PeriodTimeValueRaw> request() throws Exception;
}
