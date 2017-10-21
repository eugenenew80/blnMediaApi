package kz.kegoc.bln.producer.emcos.helper;

import kz.kegoc.bln.entity.media.day.DayMeteringBalanceRaw;

import java.time.LocalDateTime;
import java.util.List;

public interface EmcosDataService {
    List<EmcosPointCfg> requestCfg();

    List<MinuteMeteringDataRaw> requestData(String paramCode, LocalDateTime requestedDateTime);
    
    List<DayMeteringBalanceRaw> requestBalance(String paramCode, LocalDateTime requestedDateTime);
}
