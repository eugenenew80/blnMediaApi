package kz.kegoc.bln.media.producer.impl;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import kz.kegoc.bln.entity.media.dto.DailyMeteringDataDto;
import kz.kegoc.bln.media.producer.DataProducer;

@Stateless
public class ManualDataProducer implements DataProducer {

	@Inject
	public ManualDataProducer(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
	}
	
	@Schedule(second="*/5", minute = "*", hour="*")
	public void produce() {		System.out.println("ManualDataProducer started!");
		
		RBlockingQueue<DailyMeteringDataDto> queue =redissonClient.getBlockingQueue("dailyMeteringData");
		DailyMeteringDataDto data = null;
		do {			data = queue.poll();
			if (data!=null)
				System.out.println(data);
		} while (data!=null);
		
		System.out.println("ManualDataProducer finished!");
	}
	
	private RedissonClient redissonClient;
}
