package kz.kegoc.bln.loader.impl.month;

import javax.ejb.*;
import javax.inject.Inject;

import kz.kegoc.bln.loader.AbstractMeteringDataLoader;
import kz.kegoc.bln.loader.MeteringDataLoader;
import kz.kegoc.bln.entity.media.MonthMeteringDataRaw;
import kz.kegoc.bln.service.media.MeteringDataService;
import org.redisson.api.RBlockingQueue;

@Stateless
public class MonthMeteringDataRawLoaderImpl
	extends AbstractMeteringDataLoader<MonthMeteringDataRaw> 
		implements MeteringDataLoader<MonthMeteringDataRaw> {

	@Inject
	public MonthMeteringDataRawLoaderImpl(MeteringDataService<MonthMeteringDataRaw> service, RBlockingQueue<MonthMeteringDataRaw> queue) {
		super(service, queue);
	}

	public void load() {
		super.load();
	}
}
