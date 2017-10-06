package kz.kegoc.bln.loader.raw;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.*;
import javax.inject.Inject;

import kz.kegoc.bln.loader.MeteringDataLoader;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import kz.kegoc.bln.entity.media.DailyMeteringDataRaw;
import kz.kegoc.bln.repository.raw.DailyMeteringDataRawRepository;


@Singleton
@Startup
public class DailyMeteringDataRawLoader implements MeteringDataLoader {

	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		RBlockingQueue<DailyMeteringDataRaw> queue= redissonClient.getBlockingQueue("dailyMeteringData");
		List<DailyMeteringDataRaw> list = new ArrayList<>();
		while (true) {
			DailyMeteringDataRaw item = queue.poll();
			if (item==null)
				break;

			list.add(item);
		}
		repository.insertAll(list);
	}


	@Inject
	private DailyMeteringDataRawRepository repository;

	@Inject
	private RedissonClient redissonClient;
}
