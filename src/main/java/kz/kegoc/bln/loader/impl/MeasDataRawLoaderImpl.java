package kz.kegoc.bln.loader.impl;

import kz.kegoc.bln.entity.data.MeasDataRaw;
import kz.kegoc.bln.loader.AbstractMeteringDataLoader;
import kz.kegoc.bln.loader.DataLoader;
import kz.kegoc.bln.service.data.MeteringDataService;
import org.redisson.api.RBlockingQueue;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class MeasDataRawLoaderImpl
	extends AbstractMeteringDataLoader<MeasDataRaw>
		implements DataLoader<MeasDataRaw> {

	@Inject
	public MeasDataRawLoaderImpl(MeteringDataService<MeasDataRaw> service, RBlockingQueue<MeasDataRaw> queue) {
		super(service, queue);
	}

	public void load() {
		super.load();
	}
}
