package kz.kegoc.bln.service.media.raw;

import java.util.List;
import kz.kegoc.bln.entity.media.raw.DayMeteringDataRaw;
import kz.kegoc.bln.service.common.EntityService;

public interface DayMeteringDataRawService extends EntityService<DayMeteringDataRaw> {
	void saveAll(List<DayMeteringDataRaw> list);
}
