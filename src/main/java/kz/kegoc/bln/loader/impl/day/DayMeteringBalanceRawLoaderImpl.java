package kz.kegoc.bln.loader.impl.day;

import javax.ejb.*;
import javax.inject.Inject;

import kz.kegoc.bln.entity.media.day.DayMeteringBalanceRaw;
import kz.kegoc.bln.loader.AbstractMeteringDataLoader;
import kz.kegoc.bln.loader.MeteringDataLoader;
import kz.kegoc.bln.service.media.MeteringDataService;
import org.redisson.api.RBlockingQueue;

@Stateless
public class DayMeteringBalanceRawLoaderImpl
	extends AbstractMeteringDataLoader<DayMeteringBalanceRaw> 
		implements MeteringDataLoader<DayMeteringBalanceRaw> {

	@Inject
	public DayMeteringBalanceRawLoaderImpl(MeteringDataService<DayMeteringBalanceRaw> service, RBlockingQueue<DayMeteringBalanceRaw> queue) {
		super(service, queue);
	}

	public void load() {
		super.load();
	}
}
