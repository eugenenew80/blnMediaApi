package kz.kegoc.bln.loader.impl.day;

import javax.ejb.*;
import javax.inject.Inject;
import kz.kegoc.bln.loader.AbstractMeteringDataLoader;
import kz.kegoc.bln.loader.MeteringDataLoader;
import kz.kegoc.bln.entity.media.DayMeteringDataRaw;
import kz.kegoc.bln.service.media.MeteringDataService;
import org.redisson.api.RBlockingQueue;

@Stateless
public class DayMeteringDataRawLoaderImpl
	extends AbstractMeteringDataLoader<DayMeteringDataRaw> 
		implements MeteringDataLoader<DayMeteringDataRaw> {

	@Inject
	public DayMeteringDataRawLoaderImpl(MeteringDataService<DayMeteringDataRaw> service, RBlockingQueue<DayMeteringDataRaw> queue) {
		super(service, queue);
	}

	public void load() {
		super.load();
	}
}
