package kz.kegoc.bln.loader.impl;

import javax.ejb.*;
import javax.inject.Inject;

import kz.kegoc.bln.entity.data.DayMeteringBalance;
import kz.kegoc.bln.loader.AbstractMeteringDataLoader;
import kz.kegoc.bln.loader.MeteringDataLoader;
import kz.kegoc.bln.service.data.MeteringDataService;
import org.redisson.api.RBlockingQueue;

@Stateless
public class DayMeteringBalanceLoaderImpl
	extends AbstractMeteringDataLoader<DayMeteringBalance>
		implements MeteringDataLoader<DayMeteringBalance> {

	@Inject
	public DayMeteringBalanceLoaderImpl(MeteringDataService<DayMeteringBalance> service, RBlockingQueue<DayMeteringBalance> queue) {
		super(service, queue);
	}

	public void load() {
		super.load();
	}
}
