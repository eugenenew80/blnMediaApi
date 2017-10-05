package kz.kegoc.bln.service.queue;

import kz.kegoc.bln.entity.media.HourlyMeteringData;

import java.util.List;

public interface HourlyMeteringDataQueueService {
	void addMeteringData(HourlyMeteringData data);
	
	void addMeteringListData(List<HourlyMeteringData> data);

	void start();

	void shutdown();
}
