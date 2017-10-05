package kz.kegoc.bln.service.queue.impl;

import kz.kegoc.bln.entity.media.HourlyMeteringData;
import kz.kegoc.bln.service.common.AbstractMeteringDataQueueService;
import kz.kegoc.bln.service.queue.HourlyMeteringDataQueueService;
import kz.kegoc.bln.service.loader.impl.DailyMeteringDataLoader;
import kz.kegoc.bln.service.loader.impl.HourlyMeteringDataLoader;

import javax.ejb.Stateless;

@Stateless
public class HourlyMeteringDataQueueServiceImpl extends AbstractMeteringDataQueueService<HourlyMeteringData> implements HourlyMeteringDataQueueService {
	public HourlyMeteringDataQueueServiceImpl() {
		super("hourlyMeteringData");
	}
}
