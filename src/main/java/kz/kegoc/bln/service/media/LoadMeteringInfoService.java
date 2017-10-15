package kz.kegoc.bln.service.media;

import java.time.LocalDateTime;
import java.util.List;

import kz.kegoc.bln.entity.media.LoadMeteringInfo;
import kz.kegoc.bln.entity.media.MinuteMeteringDataRaw;
import kz.kegoc.bln.service.common.EntityService;

public interface LoadMeteringInfoService extends EntityService<LoadMeteringInfo> {
	void updateLoadMeteringInfo(List<MinuteMeteringDataRaw> meteringData, LocalDateTime endDateTime);
}