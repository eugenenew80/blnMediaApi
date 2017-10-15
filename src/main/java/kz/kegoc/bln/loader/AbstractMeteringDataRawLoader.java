package kz.kegoc.bln.loader;

import kz.kegoc.bln.entity.media.MeteringData;
import kz.kegoc.bln.service.media.MeteringDataService;
import org.redisson.api.RBlockingQueue;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMeteringDataRawLoader<T extends MeteringData> implements MeteringDataLoader {

	public AbstractMeteringDataRawLoader(MeteringDataService<T> service, RBlockingQueue<T> queue) {
		this.service = service;
		this.queue = queue;
	}

	public void execute() {
		List<T> list = new ArrayList<>();
		while (true) {
			T item = queue.poll();
			if (item==null)
				break;

			list.add(item);
		}
		service.saveAll(list);
	}

	private MeteringDataService<T> service;
	private RBlockingQueue<T> queue;
}
