package kz.kegoc.bln.loader.impl;

import kz.kegoc.bln.entity.media.raw.HourMeteringFlow;
import kz.kegoc.bln.loader.AbstractMeteringDataLoader;
import kz.kegoc.bln.loader.MeteringDataLoader;
import kz.kegoc.bln.service.media.raw.MeteringDataService;
import org.redisson.api.RBlockingQueue;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class HourMeteringFlowLoaderImpl
	extends AbstractMeteringDataLoader<HourMeteringFlow>
		implements MeteringDataLoader<HourMeteringFlow> {
		
	@Inject
	public HourMeteringFlowLoaderImpl(MeteringDataService<HourMeteringFlow> service, RBlockingQueue<HourMeteringFlow> queue) {
		super(service, queue);
	}

	public void load() {
		super.load();
	}
}
