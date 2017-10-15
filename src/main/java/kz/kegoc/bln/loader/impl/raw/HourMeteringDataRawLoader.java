package kz.kegoc.bln.loader.impl.raw;

import kz.kegoc.bln.entity.media.raw.HourMeteringDataRaw;
import kz.kegoc.bln.loader.AbstractMeteringDataRawLoader;
import kz.kegoc.bln.loader.MeteringDataLoader;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class HourMeteringDataRawLoader extends AbstractMeteringDataRawLoader<HourMeteringDataRaw> implements MeteringDataLoader {

	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		super.execute();
	}
}
