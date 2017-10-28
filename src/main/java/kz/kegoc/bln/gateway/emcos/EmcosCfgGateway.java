package kz.kegoc.bln.gateway.emcos;

import javax.ejb.Local;
import java.util.List;

@Local
public interface EmcosCfgGateway {
    List<EmcosPointCfg> request();
}
