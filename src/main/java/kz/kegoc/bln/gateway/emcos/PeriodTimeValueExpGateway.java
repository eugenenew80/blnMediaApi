package kz.kegoc.bln.gateway.emcos;

import kz.kegoc.bln.entity.media.ConnectionConfig;

import javax.ejb.Local;
import java.util.List;

@Local
public interface PeriodTimeValueExpGateway {
    PeriodTimeValueExpGateway config(ConnectionConfig config);
    PeriodTimeValueExpGateway points(List<MeteringPointCfg> points);
    void send() throws Exception;
}
