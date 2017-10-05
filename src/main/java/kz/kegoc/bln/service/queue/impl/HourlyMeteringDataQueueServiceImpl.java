package kz.kegoc.bln.service.queue.impl;

import kz.kegoc.bln.entity.media.HourlyMeteringData;
import kz.kegoc.bln.service.queue.HourlyMeteringDataQueueService;
import kz.kegoc.bln.service.loader.impl.DailyMeteringDataLoader;
import kz.kegoc.bln.service.loader.impl.HourlyMeteringDataLoader;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class HourlyMeteringDataQueueServiceImpl implements HourlyMeteringDataQueueService {

	@PostConstruct
	public void init() {
		queue = redissonClient.getBlockingQueue("hourlyMeteringData");
	}

	public void addMeteringData(HourlyMeteringData data) {
		queue.add(data);
	}

	public void addMeteringListData(List<HourlyMeteringData> data) {
		queue.addAll(data);
	}

	public void start() {
		HourlyMeteringDataLoader.shutdownFlag.set(false);
	}

	public void shutdown() {
		DailyMeteringDataLoader.shutdownFlag.set(true);
	}

	
	@Inject 
	private RedissonClient redissonClient;
	private RBlockingQueue<HourlyMeteringData> queue;
}
