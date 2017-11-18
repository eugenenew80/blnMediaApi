package kz.kegoc.bln.loader.impl;

import kz.kegoc.bln.entity.media.raw.HourMeteringFlowRaw;
import kz.kegoc.bln.loader.AbstractMeteringDataLoader;
import kz.kegoc.bln.loader.MeteringDataLoader;
import kz.kegoc.bln.service.media.raw.MeteringDataRawService;
import org.redisson.api.RBlockingQueue;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class HourMeteringFlowRawLoaderImpl
	extends AbstractMeteringDataLoader<HourMeteringFlowRaw>
		implements MeteringDataLoader<HourMeteringFlowRaw> {
		
	@Inject
	public HourMeteringFlowRawLoaderImpl(MeteringDataRawService<HourMeteringFlowRaw> service, RBlockingQueue<HourMeteringFlowRaw> queue) {
		super(service, queue);
	}

	public void load() {
		super.load();
	}
}
