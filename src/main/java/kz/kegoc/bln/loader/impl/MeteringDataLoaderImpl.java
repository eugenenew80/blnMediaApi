package kz.kegoc.bln.loader.impl;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import kz.kegoc.bln.entity.media.Metering;
import kz.kegoc.bln.entity.media.data.DayMeteringBalance;
import kz.kegoc.bln.entity.media.data.DayMeteringFlow;
import kz.kegoc.bln.entity.media.data.HourMeteringFlow;
import kz.kegoc.bln.entity.media.data.MonthMeteringFlow;
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
	private MeteringDataLoader<DayMeteringBalance> dayMeteringBalanceLoader;

	@Inject
	private MeteringDataLoader<DayMeteringFlow> dayMeteringDataLoader;

	@Inject
	private MeteringDataLoader<HourMeteringFlow> hourMeteringDataLoader;

	@Inject
	private MeteringDataLoader<MonthMeteringFlow> monthMeteringDataLoader;
}
