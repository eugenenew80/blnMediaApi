package kz.kegoc.bln.loader.impl;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import kz.kegoc.bln.entity.media.Metering;
import kz.kegoc.bln.entity.media.raw.DayMeteringBalanceRaw;
import kz.kegoc.bln.entity.media.raw.DayMeteringDataRaw;
import kz.kegoc.bln.entity.media.raw.HourMeteringDataRaw;
import kz.kegoc.bln.entity.media.raw.MonthMeteringDataRaw;
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
	private MeteringDataLoader<DayMeteringDataRaw> dayMeteringDataLoader;

	@Inject
	private MeteringDataLoader<HourMeteringDataRaw> hourMeteringDataLoader;

	@Inject
	private MeteringDataLoader<MonthMeteringDataRaw> monthMeteringDataLoader;
}
