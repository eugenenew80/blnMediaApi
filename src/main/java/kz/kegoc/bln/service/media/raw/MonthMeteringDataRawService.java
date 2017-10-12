package kz.kegoc.bln.service.media.raw;

import java.util.List;
import kz.kegoc.bln.entity.media.raw.MonthMeteringDataRaw;
import kz.kegoc.bln.service.common.EntityService;

public interface MonthMeteringDataRawService extends EntityService<MonthMeteringDataRaw> {
	void saveAll(List<MonthMeteringDataRaw> list);
}
