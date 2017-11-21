package kz.kegoc.bln.loader.impl;

import javax.ejb.*;
import javax.inject.Inject;

import kz.kegoc.bln.entity.data.MonthMeteringFlow;
import kz.kegoc.bln.loader.AbstractMeteringDataLoader;
import kz.kegoc.bln.loader.MeteringDataLoader;
import kz.kegoc.bln.service.data.MeteringDataService;
import org.redisson.api.RBlockingQueue;

@Stateless
public class MonthMeteringFlowLoaderImpl
	extends AbstractMeteringDataLoader<MonthMeteringFlow>
		implements MeteringDataLoader<MonthMeteringFlow> {

	@Inject
	public MonthMeteringFlowLoaderImpl(MeteringDataService<MonthMeteringFlow> service, RBlockingQueue<MonthMeteringFlow> queue) {
		super(service, queue);
	}

	public void load() {
		super.load();
	}
}
