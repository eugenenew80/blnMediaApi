package kz.kegoc.bln.service.media.data;

import java.util.List;

import kz.kegoc.bln.entity.media.data.LastLoadInfo;
import kz.kegoc.bln.entity.media.data.DayMeteringBalance;
import kz.kegoc.bln.gateway.emcos.MinuteMeteringFlow;
import kz.kegoc.bln.service.common.EntityService;

import javax.ejb.Local;

@Local
public interface LastLoadInfoService extends EntityService<LastLoadInfo> {
	void updateLastDataLoadDate(List<MinuteMeteringFlow> meteringData);
	void updateLastBalanceLoadDate(List<DayMeteringBalance> meteringData);
}
