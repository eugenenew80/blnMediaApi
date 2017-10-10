package kz.kegoc.bln.loader.raw;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.*;
import javax.inject.Inject;

import kz.kegoc.bln.loader.common.MeteringDataLoader;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import kz.kegoc.bln.entity.media.raw.MonthMeteringDataRaw;
import kz.kegoc.bln.repository.media.raw.MonthMeteringDataRawRepository;


@Singleton
@Startup
public class MonthlyMeteringDataRawLoader implements MeteringDataLoader {

	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		RBlockingQueue<MonthMeteringDataRaw> queue= redissonClient.getBlockingQueue("monthlyMeteringData");
		List<MonthMeteringDataRaw> list = new ArrayList<>();
		while (true) {
			MonthMeteringDataRaw item = queue.poll();
			if (item==null)
				break;

			list.add(item);
		}
		repository.insertAll(list);
	}


	@Inject
	private MonthMeteringDataRawRepository repository;

	@Inject
	private RedissonClient redissonClient;
}
