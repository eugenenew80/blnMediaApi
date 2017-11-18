package kz.kegoc.bln.service.media.raw;

import java.util.List;

import kz.kegoc.bln.entity.media.raw.LastLoadInfo;
import kz.kegoc.bln.entity.media.raw.DayMeteringBalanceRaw;
import kz.kegoc.bln.gateway.emcos.MinuteMeteringFlow;
import kz.kegoc.bln.service.common.EntityService;

import javax.ejb.Local;

@Local
public interface LastLoadInfoService extends EntityService<LastLoadInfo> {
	void updateLastDataLoadDate(List<MinuteMeteringFlow> meteringData);
	void updateLastBalanceLoadDate(List<DayMeteringBalanceRaw> meteringData);
}
