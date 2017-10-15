package kz.kegoc.bln.loader.impl;

import javax.ejb.*;
import javax.inject.Inject;

import kz.kegoc.bln.loader.AbstractMeteringDataRawLoader;
import kz.kegoc.bln.loader.MeteringDataLoader;
import kz.kegoc.bln.entity.media.MonthMeteringDataRaw;
import kz.kegoc.bln.service.media.MeteringDataService;
import org.redisson.api.RBlockingQueue;

@Singleton
@Startup
public class MonthMeteringDataRawLoader
	extends AbstractMeteringDataRawLoader<MonthMeteringDataRaw>
		implements MeteringDataLoader {

	@Inject
	public MonthMeteringDataRawLoader(MeteringDataService<MonthMeteringDataRaw> service, RBlockingQueue<MonthMeteringDataRaw> queue) {
		super(service, queue);
	}

	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		super.execute();
	}
}
