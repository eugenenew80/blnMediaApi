package kz.kegoc.bln.service.queue.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;

import kz.kegoc.bln.service.loader.impl.DailyMeteringDataLoader;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import kz.kegoc.bln.entity.media.DailyMeteringData;
import kz.kegoc.bln.service.queue.DailyMeteringDataQueueService;

@Stateless
public class DailyMeteringDataQueueServiceImpl implements DailyMeteringDataQueueService {

	@PostConstruct
	public void init() {
		queue = redissonClient.getBlockingQueue("dailyMeteringData");
	}

	public void addMeteringData(DailyMeteringData data) {
		queue.add(data);
	}

	public void addMeteringListData(List<DailyMeteringData> data) {
		queue.addAll(data);
	}

	public void start() {
		DailyMeteringDataLoader.shutdownFlag.set(false);
	}

	public void shutdown() {
		DailyMeteringDataLoader.shutdownFlag.set(true);
	}

	
	@Inject 
	private RedissonClient redissonClient;
	private RBlockingQueue<DailyMeteringData> queue;
}
