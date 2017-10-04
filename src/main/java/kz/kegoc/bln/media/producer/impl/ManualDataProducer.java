package kz.kegoc.bln.media.producer.impl;

import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import kz.kegoc.bln.entity.media.DailyMeteringData;
import kz.kegoc.bln.repository.media.DailyMeteringDataRepository;


@Singleton
@LocalBean
@Startup
public class ManualDataProducer {
	public static final AtomicBoolean shutdownFlag = new AtomicBoolean(false);
	
	@PostConstruct
	public void init() {
		timerService.createSingleActionTimer(5000, new TimerConfig());
	}
	
	
	@Timeout
	public void execute(Timer timer) {
		System.out.println("ManualDataProducer started!");
		
		RBlockingQueue<DailyMeteringData> queue= redissonClient.getBlockingQueue("dailyMeteringData");
		while (true) {
			if (shutdownFlag.get()) 
				break;
			
			try {
				DailyMeteringData data = queue.take();
				if (StringUtils.isNotEmpty(data.getParamCode())) { 
					repository.insert(data);
				}
			}			
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("ManualDataProducer finished!");
	}

	
	@Inject 
	private DailyMeteringDataRepository repository;
	
	@Inject 
	private RedissonClient redissonClient;
	
	@Resource 
	private TimerService timerService;
}
