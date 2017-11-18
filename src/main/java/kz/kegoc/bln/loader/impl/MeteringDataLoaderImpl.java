package kz.kegoc.bln.loader.impl;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import kz.kegoc.bln.entity.media.Metering;
import kz.kegoc.bln.entity.media.raw.DayMeteringBalanceRaw;
import kz.kegoc.bln.entity.media.raw.DayMeteringFlowRaw;
import kz.kegoc.bln.entity.media.raw.HourMeteringFlowRaw;
import kz.kegoc.bln.entity.media.raw.MonthMeteringFlowRaw;
import kz.kegoc.bln.loader.MeteringDataLoader;

@Singleton
public class MeteringDataLoaderImpl implements MeteringDataLoader<Metering> {

	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void load() {
		hourMeteringDataLoader.load();
		dayMeteringDataLoader.load();
		monthMeteringDataLoader.load();
		dayMeteringBalanceLoader.load();
	}

	@Inject
	private MeteringDataLoader<DayMeteringBalanceRaw> dayMeteringBalanceLoader;

	@Inject
	private MeteringDataLoader<DayMeteringFlowRaw> dayMeteringDataLoader;

	@Inject
	private MeteringDataLoader<HourMeteringFlowRaw> hourMeteringDataLoader;

	@Inject
	private MeteringDataLoader<MonthMeteringFlowRaw> monthMeteringDataLoader;
}
