package kz.kegoc.bln.loader.impl;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import kz.kegoc.bln.entity.common.Metering;
import kz.kegoc.bln.entity.data.DayMeteringBalance;
import kz.kegoc.bln.entity.data.DayMeteringFlow;
import kz.kegoc.bln.entity.data.HourMeteringFlow;
import kz.kegoc.bln.entity.data.MonthMeteringFlow;
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
