package kz.kegoc.bln.service.raw;

import java.time.LocalDateTime;
import java.util.List;

import kz.kegoc.bln.entity.media.HourlyMeteringDataRaw;
import kz.kegoc.bln.entity.media.LoadMeteringInfo;
import kz.kegoc.bln.service.common.EntityService;

public interface LoadMeteringInfoService extends EntityService<LoadMeteringInfo> {
	void updateLoadMeteringInfo(List<HourlyMeteringDataRaw> meteringData, LocalDateTime endDateTime);
}
