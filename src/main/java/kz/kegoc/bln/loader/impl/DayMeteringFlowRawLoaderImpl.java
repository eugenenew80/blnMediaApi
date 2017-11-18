package kz.kegoc.bln.loader.impl;

import javax.ejb.*;
import javax.inject.Inject;

import kz.kegoc.bln.entity.media.raw.DayMeteringFlowRaw;
import kz.kegoc.bln.loader.AbstractMeteringDataLoader;
import kz.kegoc.bln.loader.MeteringDataLoader;
import kz.kegoc.bln.service.media.raw.MeteringDataRawService;
import org.redisson.api.RBlockingQueue;

@Stateless
public class DayMeteringFlowRawLoaderImpl
	extends AbstractMeteringDataLoader<DayMeteringFlowRaw>
		implements MeteringDataLoader<DayMeteringFlowRaw> {

	@Inject
	public DayMeteringFlowRawLoaderImpl(MeteringDataRawService<DayMeteringFlowRaw> service, RBlockingQueue<DayMeteringFlowRaw> queue) {
		super(service, queue);
	}

	public void load() {
		super.load();
	}
}
