package kz.kegoc.bln.service.queue.impl;

import javax.ejb.Stateless;
import kz.kegoc.bln.service.queue.common.AbstractMeteringDataQueueService;
import kz.kegoc.bln.entity.media.MonthlyMeteringData;
import kz.kegoc.bln.service.queue.MonthlyMeteringDataQueueService;

@Stateless
public class MonthlyMeteringDataQueueServiceImpl extends AbstractMeteringDataQueueService<MonthlyMeteringData> implements MonthlyMeteringDataQueueService {
	public MonthlyMeteringDataQueueServiceImpl() {
		super("monthlyMeteringData");
	}
}
