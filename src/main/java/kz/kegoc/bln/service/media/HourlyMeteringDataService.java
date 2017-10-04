package kz.kegoc.bln.service.media;

import kz.kegoc.bln.entity.media.HourlyMeteringData;

import java.util.List;

public interface HourlyMeteringDataService {
	void addMeteringData(HourlyMeteringData data);
	
	void addMeteringListData(List<HourlyMeteringData> data);

	void start();

	void shutdown();
}
