package kz.kegoc.bln.service.data;

import java.util.List;

import kz.kegoc.bln.entity.data.LastLoadInfo;
import kz.kegoc.bln.entity.data.DayMeteringBalance;
import kz.kegoc.bln.gateway.emcos.MinuteMeteringFlow;
import kz.kegoc.bln.service.common.EntityService;

import javax.ejb.Local;

@Local
public interface LastLoadInfoService extends EntityService<LastLoadInfo> {
	void updateLastDataLoadDate(List<MinuteMeteringFlow> meteringData);
	void updateLastBalanceLoadDate(List<DayMeteringBalance> meteringData);
}
