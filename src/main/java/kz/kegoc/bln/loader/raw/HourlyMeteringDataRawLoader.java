package kz.kegoc.bln.loader.raw;

import kz.kegoc.bln.entity.media.HourlyMeteringDataRaw;
import kz.kegoc.bln.repository.raw.HourlyMeteringDataRawRepository;
import kz.kegoc.bln.loader.MeteringDataLoader;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


@Singleton
@Startup
public class HourlyMeteringDataRawLoader implements MeteringDataLoader {

	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		RBlockingQueue<HourlyMeteringDataRaw> queue= redissonClient.getBlockingQueue("hourlyMeteringData");
		List<HourlyMeteringDataRaw> list = new ArrayList<>();
		while (true) {
			HourlyMeteringDataRaw item = queue.poll();
			if (item==null)
				break;

			list.add(item);
		}
		repository.insertAll(list);
	}


	@Inject
	private HourlyMeteringDataRawRepository repository;

	@Inject
	private RedissonClient redissonClient;
}