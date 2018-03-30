package kz.kegoc.bln.gateway.emcos;

import kz.kegoc.bln.imp.raw.AtTimeValueRaw;
import kz.kegoc.bln.entity.media.ConnectionConfig;

import javax.ejb.Local;
import java.util.List;

@Local
public interface AtTimeValueGateway {
    AtTimeValueGateway config(ConnectionConfig config);

    AtTimeValueGateway points(List<MeteringPointCfg> points);

    List<AtTimeValueRaw> request() throws Exception;
}
