package kz.kegoc.bln.service.data;

import java.util.List;
import kz.kegoc.bln.entity.data.LastLoadInfo;
import kz.kegoc.bln.entity.data.MeteringReadingRaw;
import kz.kegoc.bln.entity.data.MeasDataRaw;
import kz.kegoc.bln.service.common.EntityService;
import javax.ejb.Local;

@Local
public interface LastLoadInfoService extends EntityService<LastLoadInfo> {
	void updateLastDataLoadDate(List<MeasDataRaw> meteringData);
	void updateLastBalanceLoadDate(List<MeteringReadingRaw> meteringData);
}
