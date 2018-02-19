package kz.kegoc.bln.gateway.emcos;

import kz.kegoc.bln.entity.data.ConnectionConfig;
import kz.kegoc.bln.entity.data.PowerConsumptionRaw;
import javax.ejb.Local;
import java.util.List;

@Local
public interface PowerConsumptionGateway {
    PowerConsumptionGateway config(ConnectionConfig config);

    PowerConsumptionGateway points(List<MeteringPointCfg> points);

    List<PowerConsumptionRaw> request();
}
