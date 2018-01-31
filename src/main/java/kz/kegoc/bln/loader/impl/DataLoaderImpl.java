package kz.kegoc.bln.loader.impl;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import kz.kegoc.bln.entity.common.Metering;
import kz.kegoc.bln.entity.data.MeasDataRaw;
import kz.kegoc.bln.entity.data.MeteringReadingRaw;
import kz.kegoc.bln.loader.DataLoader;

@Singleton
public class DataLoaderImpl implements DataLoader<Metering> {

	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void load() {
		measDataRawLoader.load();
		meteringReadingMeteringDataLoader.load();
	}

	@Inject
	private DataLoader<MeteringReadingRaw> meteringReadingMeteringDataLoader;


	@Inject
	private DataLoader<MeasDataRaw> measDataRawLoader;
}
