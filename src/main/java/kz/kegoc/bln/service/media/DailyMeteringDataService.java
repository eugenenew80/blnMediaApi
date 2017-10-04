package kz.kegoc.bln.service.media;

import java.util.List;

import kz.kegoc.bln.entity.media.DailyMeteringData;

public interface DailyMeteringDataService {
	void addMeteringData(DailyMeteringData data);
	
	void addMeteringListData(List<DailyMeteringData> data);

	void start();

	void shutdown();
}
