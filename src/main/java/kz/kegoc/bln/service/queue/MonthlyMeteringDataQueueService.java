package kz.kegoc.bln.service.queue;

import java.util.List;

import kz.kegoc.bln.entity.media.MonthlyMeteringData;

public interface MonthlyMeteringDataQueueService {
	void addMeteringData(MonthlyMeteringData data);
	
	void addMeteringListData(List<MonthlyMeteringData> data);

	void start();

	void shutdown();
}
