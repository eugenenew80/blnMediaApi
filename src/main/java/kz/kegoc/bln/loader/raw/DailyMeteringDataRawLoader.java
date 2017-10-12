package kz.kegoc.bln.loader.raw;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.*;
import javax.inject.Inject;

import kz.kegoc.bln.loader.common.MeteringDataLoader;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import kz.kegoc.bln.entity.media.raw.DayMeteringDataRaw;
import kz.kegoc.bln.service.media.raw.DayMeteringDataRawService;


@Singleton
@Startup
public class DailyMeteringDataRawLoader implements MeteringDataLoader {

	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		RBlockingQueue<DayMeteringDataRaw> queue= redissonClient.getBlockingQueue("dailyMeteringData");
		List<DayMeteringDataRaw> list = new ArrayList<>();
		while (true) {
			DayMeteringDataRaw item = queue.poll();
			if (item==null)
				break;

			list.add(item);
		}
		service.saveAll(list);
	}


	@Inject
	private DayMeteringDataRawService service;

	@Inject
	private RedissonClient redissonClient;
}
