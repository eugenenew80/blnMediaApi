package kz.kegoc.bln.loader.impl;

import javax.ejb.*;
import javax.inject.Inject;

import kz.kegoc.bln.entity.data.MeteringReading;
import kz.kegoc.bln.loader.AbstractMeteringDataLoader;
import kz.kegoc.bln.loader.MeteringDataLoader;
import kz.kegoc.bln.service.data.MeteringDataService;
import org.redisson.api.RBlockingQueue;

@Stateless
public class MeteringReadingLoaderImpl
	extends AbstractMeteringDataLoader<MeteringReading>
		implements MeteringDataLoader<MeteringReading> {

	@Inject
	public MeteringReadingLoaderImpl(MeteringDataService<MeteringReading> service, RBlockingQueue<MeteringReading> queue) {
		super(service, queue);
	}

	public void load() {
		super.load();
	}
}
