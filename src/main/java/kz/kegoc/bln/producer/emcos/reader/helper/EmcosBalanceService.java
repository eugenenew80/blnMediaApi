package kz.kegoc.bln.producer.emcos.reader.helper;

import kz.kegoc.bln.entity.media.day.DayMeteringBalanceRaw;

import java.time.LocalDateTime;
import java.util.List;

public interface EmcosBalanceService {
    List<DayMeteringBalanceRaw> request(String paramCode, LocalDateTime requestedDateTime);
}
