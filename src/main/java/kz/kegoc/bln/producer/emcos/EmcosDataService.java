package kz.kegoc.bln.producer.emcos;

import kz.kegoc.bln.entity.media.raw.EmcosMeteringPointCfg;
import kz.kegoc.bln.entity.media.raw.MinuteMeteringDataRaw;
import java.util.List;

public interface EmcosDataService {
    List<EmcosMeteringPointCfg> requestCfg() throws Exception;

    List<MinuteMeteringDataRaw> requestMeteringData() throws Exception;
}
