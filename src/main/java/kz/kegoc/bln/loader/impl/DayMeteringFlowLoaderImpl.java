package kz.kegoc.bln.loader.impl;

import javax.ejb.*;
import javax.inject.Inject;

import kz.kegoc.bln.entity.media.raw.DayMeteringFlow;
import kz.kegoc.bln.loader.AbstractMeteringDataLoader;
import kz.kegoc.bln.loader.MeteringDataLoader;
import kz.kegoc.bln.service.media.raw.MeteringDataService;
import org.redisson.api.RBlockingQueue;

@Stateless
public class DayMeteringFlowLoaderImpl
	extends AbstractMeteringDataLoader<DayMeteringFlow>
		implements MeteringDataLoader<DayMeteringFlow> {

	@Inject
	public DayMeteringFlowLoaderImpl(MeteringDataService<DayMeteringFlow> service, RBlockingQueue<DayMeteringFlow> queue) {
		super(service, queue);
	}

	public void load() {
		super.load();
	}
}
