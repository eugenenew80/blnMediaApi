package kz.kegoc.bln.service.queue.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;

import kz.kegoc.bln.service.loader.impl.MonthlyMeteringDataLoader;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import kz.kegoc.bln.entity.media.MonthlyMeteringData;
import kz.kegoc.bln.service.queue.MonthlyMeteringDataQueueService;

@Stateless
public class MonthlyMeteringDataQueueServiceImpl implements MonthlyMeteringDataQueueService {

	@PostConstruct
	public void init() {
		queue = redissonClient.getBlockingQueue("monthlyMeteringData");
	}

	public void addMeteringData(MonthlyMeteringData data) {
		queue.add(data);
	}

	public void addMeteringListData(List<MonthlyMeteringData> data) {
		queue.addAll(data);
	}

	public void start() {
		MonthlyMeteringDataLoader.shutdownFlag.set(false);
	}

	public void shutdown() {
		MonthlyMeteringDataLoader.shutdownFlag.set(true);
	}

	
	@Inject 
	private RedissonClient redissonClient;
	private RBlockingQueue<MonthlyMeteringData> queue;
}
