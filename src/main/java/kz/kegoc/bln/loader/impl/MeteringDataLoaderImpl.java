package kz.kegoc.bln.loader.impl;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import kz.kegoc.bln.entity.common.Metering;
import kz.kegoc.bln.entity.data.MeasData;
import kz.kegoc.bln.entity.data.MeasDataRaw;
import kz.kegoc.bln.entity.data.MeteringReading;
import kz.kegoc.bln.loader.MeteringDataLoader;

@Singleton
public class MeteringDataLoaderImpl implements MeteringDataLoader<Metering> {

	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void load() {
		measDataRawLoader.load();
		measDataLoader.load();
		meteringReadingMeteringDataLoader.load();
	}

	@Inject
	private MeteringDataLoader<MeteringReading> meteringReadingMeteringDataLoader;

	@Inject
	private MeteringDataLoader<MeasData> measDataLoader;

	@Inject
	private MeteringDataLoader<MeasDataRaw> measDataRawLoader;
}
