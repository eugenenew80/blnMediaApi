package kz.kegoc.bln.service.media.raw;

import java.util.List;

import kz.kegoc.bln.entity.media.raw.LastLoadInfo;
import kz.kegoc.bln.entity.media.raw.DayMeteringBalanceRaw;
import kz.kegoc.bln.gateway.emcos.MinuteMeteringData;
import kz.kegoc.bln.service.common.EntityService;

public interface LastLoadInfoService extends EntityService<LastLoadInfo> {
	void updateLastDataLoadDate(List<MinuteMeteringData> meteringData);
	void updateLastBalanceLoadDate(List<DayMeteringBalanceRaw> meteringData);
}
