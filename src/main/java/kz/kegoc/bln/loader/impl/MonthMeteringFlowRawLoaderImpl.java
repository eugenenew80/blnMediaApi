package kz.kegoc.bln.loader.impl;

import javax.ejb.*;
import javax.inject.Inject;

import kz.kegoc.bln.entity.media.raw.MonthMeteringFlowRaw;
import kz.kegoc.bln.loader.AbstractMeteringDataLoader;
import kz.kegoc.bln.loader.MeteringDataLoader;
import kz.kegoc.bln.service.media.raw.MeteringDataRawService;
import org.redisson.api.RBlockingQueue;

@Stateless
public class MonthMeteringFlowRawLoaderImpl
	extends AbstractMeteringDataLoader<MonthMeteringFlowRaw>
		implements MeteringDataLoader<MonthMeteringFlowRaw> {

	@Inject
	public MonthMeteringFlowRawLoaderImpl(MeteringDataRawService<MonthMeteringFlowRaw> service, RBlockingQueue<MonthMeteringFlowRaw> queue) {
		super(service, queue);
	}

	public void load() {
		super.load();
	}
}
