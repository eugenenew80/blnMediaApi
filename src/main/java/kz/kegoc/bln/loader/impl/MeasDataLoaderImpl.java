package kz.kegoc.bln.loader.impl;

import kz.kegoc.bln.entity.data.MeasData;
import kz.kegoc.bln.loader.AbstractMeteringDataLoader;
import kz.kegoc.bln.loader.MeteringDataLoader;
import kz.kegoc.bln.service.data.MeteringDataService;
import org.redisson.api.RBlockingQueue;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class MeasDataLoaderImpl
	extends AbstractMeteringDataLoader<MeasData>
		implements MeteringDataLoader<MeasData> {
		
	@Inject
	public MeasDataLoaderImpl(MeteringDataService<MeasData> service, RBlockingQueue<MeasData> queue) {
		super(service, queue);
	}

	public void load() {
		super.load();
	}
}
