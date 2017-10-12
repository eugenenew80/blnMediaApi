package kz.kegoc.bln.loader.raw;

import kz.kegoc.bln.entity.media.raw.HourMeteringDataRaw;
import kz.kegoc.bln.service.media.raw.HourMeteringDataRawService;
import kz.kegoc.bln.loader.common.MeteringDataLoader;
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
		RBlockingQueue<HourMeteringDataRaw> queue= redissonClient.getBlockingQueue("hourlyMeteringData");
		List<HourMeteringDataRaw> list = new ArrayList<>();
		while (true) {
			HourMeteringDataRaw item = queue.poll();
			if (item==null)
				break;

			list.add(item);
		}
		service.saveAll(list);
	}


	@Inject
	private HourMeteringDataRawService service;

	@Inject
	private RedissonClient redissonClient;
}
