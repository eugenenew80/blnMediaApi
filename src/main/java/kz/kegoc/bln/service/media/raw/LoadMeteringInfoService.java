package kz.kegoc.bln.service.media.raw;

import java.time.LocalDateTime;
import java.util.List;

import kz.kegoc.bln.entity.media.raw.LoadMeteringInfo;
import kz.kegoc.bln.entity.media.raw.MinuteMeteringDataRaw;
import kz.kegoc.bln.service.common.EntityService;

public interface LoadMeteringInfoService extends EntityService<LoadMeteringInfo> {
	void updateLoadMeteringInfo(List<MinuteMeteringDataRaw> meteringData, LocalDateTime endDateTime);
}
