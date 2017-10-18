package kz.kegoc.bln.producer.emcos.helper;

import kz.kegoc.bln.entity.media.DayMeteringBalanceRaw;
import kz.kegoc.bln.entity.media.EmcosMeteringPointCfg;
import kz.kegoc.bln.entity.media.MinuteMeteringDataRaw;
import java.util.List;

public interface EmcosDataService {
    List<EmcosMeteringPointCfg> requestCfg() throws Exception;

    List<MinuteMeteringDataRaw> requestMeteringData() throws Exception;
    
    List<DayMeteringBalanceRaw> requestMeteringBalance() throws Exception;
}
