package kz.kegoc.bln.service.queue.impl;

import javax.ejb.Stateless;
import kz.kegoc.bln.service.common.AbstractMeteringDataQueueService;
import kz.kegoc.bln.entity.media.DailyMeteringData;
import kz.kegoc.bln.service.queue.DailyMeteringDataQueueService;

@Stateless
public class DailyMeteringDataQueueServiceImpl extends AbstractMeteringDataQueueService<DailyMeteringData> implements DailyMeteringDataQueueService {
	public DailyMeteringDataQueueServiceImpl() {
		super("dailyMeteringData");
	}
}
