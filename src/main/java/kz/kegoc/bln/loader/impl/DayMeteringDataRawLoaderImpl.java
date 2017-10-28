package kz.kegoc.bln.loader.impl;

import javax.ejb.*;
import javax.inject.Inject;

import kz.kegoc.bln.entity.media.raw.DayMeteringDataRaw;
import kz.kegoc.bln.loader.AbstractMeteringDataLoader;
import kz.kegoc.bln.loader.MeteringDataLoader;
import kz.kegoc.bln.service.media.raw.MeteringDataRawService;
import org.redisson.api.RBlockingQueue;

@Stateless
public class DayMeteringDataRawLoaderImpl
	extends AbstractMeteringDataLoader<DayMeteringDataRaw> 
		implements MeteringDataLoader<DayMeteringDataRaw> {

	@Inject
	public DayMeteringDataRawLoaderImpl(MeteringDataRawService<DayMeteringDataRaw> service, RBlockingQueue<DayMeteringDataRaw> queue) {
		super(service, queue);
	}

	public void load() {
		super.load();
	}
}
