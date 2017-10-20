package kz.kegoc.bln.service.media;

import java.util.List;

import kz.kegoc.bln.entity.media.LastLoadInfo;
import kz.kegoc.bln.entity.media.day.DayMeteringBalanceRaw;
import kz.kegoc.bln.producer.emcos.helper.MinuteMeteringDataRaw;
import kz.kegoc.bln.service.common.EntityService;

public interface LastLoadInfoService extends EntityService<LastLoadInfo> {
	void updateLastDataLoadDate(List<MinuteMeteringDataRaw> meteringData);
	void updateLastBalanceLoadDate(List<DayMeteringBalanceRaw> meteringData);
}
