package kz.kegoc.bln.loader.impl;

import kz.kegoc.bln.entity.data.MeteringReadingRaw;
import kz.kegoc.bln.loader.AbstractMeteringDataLoader;
import kz.kegoc.bln.loader.DataLoader;
import kz.kegoc.bln.service.data.MeteringDataService;
import org.redisson.api.RBlockingQueue;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class MeteringReadingRawLoaderImpl
	extends AbstractMeteringDataLoader<MeteringReadingRaw>
		implements DataLoader<MeteringReadingRaw> {

	@Inject
	public MeteringReadingRawLoaderImpl(MeteringDataService<MeteringReadingRaw> service, RBlockingQueue<MeteringReadingRaw> queue) {
		super(service, queue);
	}

	public void load() {
		super.load();
	}
}
