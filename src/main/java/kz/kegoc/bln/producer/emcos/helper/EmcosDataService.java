package kz.kegoc.bln.producer.emcos.helper;

import kz.kegoc.bln.entity.media.day.DayMeteringBalanceRaw;

import java.util.List;

public interface EmcosDataService {
    List<EmcosPointParamCfg> requestCfg() throws Exception;

    List<MinuteMeteringDataRaw> requestMeteringData() throws Exception;
    
    List<DayMeteringBalanceRaw> requestMeteringBalance() throws Exception;
}
