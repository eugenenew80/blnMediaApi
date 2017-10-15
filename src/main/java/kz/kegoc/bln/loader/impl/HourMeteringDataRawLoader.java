package kz.kegoc.bln.loader.impl;

import kz.kegoc.bln.entity.media.HourMeteringDataRaw;
import kz.kegoc.bln.loader.AbstractMeteringDataRawLoader;
import kz.kegoc.bln.loader.MeteringDataLoader;
import kz.kegoc.bln.service.media.MeteringDataService;
import org.redisson.api.RBlockingQueue;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
public class HourMeteringDataRawLoader
	extends AbstractMeteringDataRawLoader<HourMeteringDataRaw>
		implements MeteringDataLoader {

	@Inject
	public HourMeteringDataRawLoader(MeteringDataService<HourMeteringDataRaw> service, RBlockingQueue<HourMeteringDataRaw> queue) {
		super(service, queue);
	}

	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		super.execute();
	}
}
