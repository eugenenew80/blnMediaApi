package kz.kegoc.bln.loader.impl.hour;

import kz.kegoc.bln.entity.media.HourMeteringDataRaw;
import kz.kegoc.bln.loader.AbstractMeteringDataLoader;
import kz.kegoc.bln.loader.MeteringDataLoader;
import kz.kegoc.bln.service.media.MeteringDataService;
import org.redisson.api.RBlockingQueue;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class HourMeteringDataRawLoaderImpl
	extends AbstractMeteringDataLoader<HourMeteringDataRaw> 
		implements MeteringDataLoader<HourMeteringDataRaw> {
		
	@Inject
	public HourMeteringDataRawLoaderImpl(MeteringDataService<HourMeteringDataRaw> service, RBlockingQueue<HourMeteringDataRaw> queue) {
		super(service, queue);
	}

	public void load() {
		super.load();
	}
}
