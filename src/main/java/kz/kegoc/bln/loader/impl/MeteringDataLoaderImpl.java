package kz.kegoc.bln.loader.impl;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import kz.kegoc.bln.entity.media.MeteringData;
import kz.kegoc.bln.entity.media.day.DayMeteringBalanceRaw;
import kz.kegoc.bln.entity.media.day.DayMeteringDataRaw;
import kz.kegoc.bln.entity.media.hour.HourMeteringDataRaw;
import kz.kegoc.bln.entity.media.month.MonthMeteringDataRaw;
import kz.kegoc.bln.loader.MeteringDataLoader;

@Startup
@Singleton
public class MeteringDataLoaderImpl implements MeteringDataLoader<MeteringData> {

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
