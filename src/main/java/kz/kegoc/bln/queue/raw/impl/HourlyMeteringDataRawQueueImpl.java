package kz.kegoc.bln.queue.raw.impl;

import kz.kegoc.bln.entity.media.raw.HourMeteringDataRaw;
import kz.kegoc.bln.queue.common.AbstractMeteringDataQueueService;
import kz.kegoc.bln.queue.raw.HourlyMeteringDataRawQueue;

import javax.ejb.Stateless;

@Stateless
public class HourlyMeteringDataRawQueueImpl extends AbstractMeteringDataQueueService<HourMeteringDataRaw> implements HourlyMeteringDataRawQueue {
	public HourlyMeteringDataRawQueueImpl() {
		super("hourlyMeteringData");
	}
}
