package kz.kegoc.bln.loader;

import kz.kegoc.bln.entity.media.Metering;
import kz.kegoc.bln.service.media.raw.MeteringDataRawService;
import org.redisson.api.RBlockingQueue;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMeteringDataLoader<T extends Metering> implements MeteringDataLoader<T> {

	public AbstractMeteringDataLoader(MeteringDataRawService<T> service, RBlockingQueue<T> queue) {
		this.service = service;
		this.queue = queue;
	}

	public void load() {
		List<T> list = new ArrayList<>();
		while (true) {
			T item = queue.poll();
			if (item==null)
				break;

			list.add(item);
		}
		service.saveAll(list);
	}

	private MeteringDataRawService<T> service;
	private RBlockingQueue<T> queue;
}
