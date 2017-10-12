package kz.kegoc.bln.service.media.raw;

import java.util.List;
import kz.kegoc.bln.entity.media.raw.HourMeteringDataRaw;
import kz.kegoc.bln.service.common.EntityService;

public interface HourMeteringDataRawService extends EntityService<HourMeteringDataRaw> {
	void saveAll(List<HourMeteringDataRaw> list);
}
