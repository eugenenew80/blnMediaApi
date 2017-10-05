package kz.kegoc.bln.service.loader.impl;

import kz.kegoc.bln.entity.media.HourlyMeteringData;
import kz.kegoc.bln.repository.media.HourlyMeteringDataRepository;
import kz.kegoc.bln.service.loader.MeteringDataLoader;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


@Singleton
@Startup
public class HourlyMeteringDataLoader implements MeteringDataLoader {

	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		RBlockingQueue<HourlyMeteringData> queue= redissonClient.getBlockingQueue("hourlyMeteringData");
		List<HourlyMeteringData> list = new ArrayList<>();
		while (true) {
			HourlyMeteringData item = queue.poll();
			if (item==null)
				break;

			list.add(item);
		}
		repository.insertAll(list);
	}


	@Inject
	private HourlyMeteringDataRepository repository;

	@Inject
	private RedissonClient redissonClient;
}
