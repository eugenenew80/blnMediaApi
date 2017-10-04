package kz.kegoc.bln.media.producer.impl;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import kz.kegoc.bln.entity.media.dto.DailyMeteringDataDto;


@Singleton
@LocalBean
@Startup
public class ManualDataProducer {

	@PostConstruct
	public void init() {
		TimerConfig timerConfig = new TimerConfig();
		timerConfig.setPersistent(false);
		timerService.createSingleActionTimer(5000, timerConfig);
	}


	@Timeout
	public void execute(Timer timer) {
		System.out.println("ManualDataProducer started!");
		
		RBlockingQueue<DailyMeteringDataDto> queue =redissonClient.getBlockingQueue("dailyMeteringData");
		DailyMeteringDataDto data;
		while (true) {
			try {
				data = queue.take();
				System.out.println(data);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Inject private RedissonClient redissonClient;
	@Resource private TimerService timerService;
}
