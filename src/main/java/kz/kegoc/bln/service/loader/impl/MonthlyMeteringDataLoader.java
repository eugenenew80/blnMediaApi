package kz.kegoc.bln.service.loader.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.ejb.*;
import javax.inject.Inject;

import kz.kegoc.bln.service.loader.MeteringDataLoader;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import kz.kegoc.bln.entity.media.MonthlyMeteringData;
import kz.kegoc.bln.repository.media.MonthlyMeteringDataRepository;


@Singleton
@Startup
public class MonthlyMeteringDataLoader implements MeteringDataLoader {
	public static final AtomicBoolean shutdownFlag = new AtomicBoolean(false);

	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		if (shutdownFlag.get())
			return;

		RBlockingQueue<MonthlyMeteringData> queue= redissonClient.getBlockingQueue("monthlyMeteringData");
		List<MonthlyMeteringData> list = new ArrayList<>();
		while (true) {
			MonthlyMeteringData item = queue.poll();
			if (item==null)
				break;

			list.add(item);
		}
		repository.insertAll(list);
	}


	@Inject
	private MonthlyMeteringDataRepository repository;

	@Inject
	private RedissonClient redissonClient;
}
