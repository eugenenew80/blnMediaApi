package kz.kegoc.bln.loader.impl;

import javax.ejb.*;
import javax.inject.Inject;

import kz.kegoc.bln.loader.AbstractMeteringDataLoader;
import kz.kegoc.bln.loader.MeteringDataLoader;
import kz.kegoc.bln.entity.media.DayMeteringDataRaw;
import kz.kegoc.bln.service.media.MeteringDataService;
import org.redisson.api.RBlockingQueue;

@Singleton
@Startup
public class DayMeteringDataRawLoaderImpl
	extends AbstractMeteringDataLoader<DayMeteringDataRaw>
		implements MeteringDataLoader {

	@Inject
	public DayMeteringDataRawLoaderImpl(MeteringDataService<DayMeteringDataRaw> service, RBlockingQueue<DayMeteringDataRaw> queue) {
		super(service, queue);
	}

	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		super.execute();
	}
}
