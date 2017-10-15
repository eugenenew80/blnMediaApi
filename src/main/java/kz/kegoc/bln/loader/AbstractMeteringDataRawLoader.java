package kz.kegoc.bln.loader;

import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.service.media.raw.MeteringDataService;
import org.redisson.api.RBlockingQueue;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMeteringDataRawLoader<T extends HasId> implements MeteringDataLoader {
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

	@Inject
	private MeteringDataService<T> service;

	@Inject
	private RBlockingQueue<T> queue;
}
