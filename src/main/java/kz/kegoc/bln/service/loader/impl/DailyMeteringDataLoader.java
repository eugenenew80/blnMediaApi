package kz.kegoc.bln.service.loader.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.ejb.*;
import javax.inject.Inject;

import kz.kegoc.bln.service.loader.MeteringDataLoader;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import kz.kegoc.bln.entity.media.DailyMeteringData;
import kz.kegoc.bln.repository.media.DailyMeteringDataRepository;


@Singleton
@Startup
public class DailyMeteringDataLoader implements MeteringDataLoader {

	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
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
