package kz.kegoc.bln.loader.raw;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.*;
import javax.inject.Inject;

import kz.kegoc.bln.loader.MeteringDataLoader;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import kz.kegoc.bln.entity.media.MonthlyMeteringDataRaw;
import kz.kegoc.bln.repository.raw.MonthlyMeteringDataRawRepository;


@Singleton
@Startup
public class MonthlyMeteringDataRawLoader implements MeteringDataLoader {

	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		RBlockingQueue<MonthlyMeteringDataRaw> queue= redissonClient.getBlockingQueue("monthlyMeteringData");
		List<MonthlyMeteringDataRaw> list = new ArrayList<>();
		while (true) {
			MonthlyMeteringDataRaw item = queue.poll();
			if (item==null)
				break;

			list.add(item);
		}
		repository.insertAll(list);
	}


	@Inject
	private MonthlyMeteringDataRawRepository repository;

	@Inject
	private RedissonClient redissonClient;
}
