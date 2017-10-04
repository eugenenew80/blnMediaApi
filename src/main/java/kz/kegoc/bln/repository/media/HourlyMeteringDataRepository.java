package kz.kegoc.bln.repository.media;

import kz.kegoc.bln.entity.media.HourlyMeteringData;

import java.util.List;

public interface HourlyMeteringDataRepository {
	HourlyMeteringData insert(HourlyMeteringData entity);

	void insertAll(List<HourlyMeteringData> list);
}
