package kz.kegoc.bln.service.media.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.ejb.*;
import javax.inject.Inject;

import kz.kegoc.bln.service.media.MeteringDataProducer;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import kz.kegoc.bln.entity.media.DailyMeteringData;
import kz.kegoc.bln.repository.media.DailyMeteringDataRepository;


@Singleton
@Startup
public class UserFormMeteringDataProducer implements MeteringDataProducer {
	public static final AtomicBoolean shutdownFlag = new AtomicBoolean(false);

	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		if (shutdownFlag.get())
			return;

		RBlockingQueue<DailyMeteringData> queue= redissonClient.getBlockingQueue("dailyMeteringData");
		List<DailyMeteringData> list = new ArrayList<>();
		while (true) {
			DailyMeteringData item = queue.poll();
			if (item==null)
				break;

			list.add(item);
		}
		repository.insertAll(list);
	}


	@Inject
	private DailyMeteringDataRepository repository;

	@Inject
	private RedissonClient redissonClient;
}
