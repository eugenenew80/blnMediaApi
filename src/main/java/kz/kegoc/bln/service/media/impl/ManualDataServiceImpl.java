package kz.kegoc.bln.service.media.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import kz.kegoc.bln.entity.media.DailyMeteringData;
import kz.kegoc.bln.media.producer.impl.ManualDataProducer;
import kz.kegoc.bln.service.media.ManualDataService;

@Stateless
public class ManualDataServiceImpl implements ManualDataService {

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
	
	public void shutdown() {
		ManualDataProducer.shutdownFlag.set(true);
		queue.offer(new DailyMeteringData());		
	}

	
	@Inject 
	private RedissonClient redissonClient;
	private RBlockingQueue<DailyMeteringData> queue;
}
