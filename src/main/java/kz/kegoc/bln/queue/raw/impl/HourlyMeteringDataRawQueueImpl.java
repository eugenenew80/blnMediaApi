package kz.kegoc.bln.queue.raw.impl;

import kz.kegoc.bln.entity.media.HourlyMeteringDataRaw;
import kz.kegoc.bln.queue.common.AbstractMeteringDataQueueService;
import kz.kegoc.bln.queue.raw.HourlyMeteringDataRawQueue;

import javax.ejb.Stateless;

@Stateless
public class HourlyMeteringDataRawQueueImpl extends AbstractMeteringDataQueueService<HourlyMeteringDataRaw> implements HourlyMeteringDataRawQueue {
	public HourlyMeteringDataRawQueueImpl() {
		super("hourlyMeteringData");
	}
}
