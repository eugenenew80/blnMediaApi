package kz.kegoc.bln.service.queue;

import java.util.List;

import kz.kegoc.bln.entity.media.DailyMeteringData;

public interface DailyMeteringDataQueueService {
	void addMeteringData(DailyMeteringData data);
	
	void addMeteringListData(List<DailyMeteringData> data);

	void start();

	void shutdown();
}
