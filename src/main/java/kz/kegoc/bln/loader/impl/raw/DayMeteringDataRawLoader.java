package kz.kegoc.bln.loader.impl.raw;

import javax.ejb.*;

import kz.kegoc.bln.loader.AbstractMeteringDataRawLoader;
import kz.kegoc.bln.loader.MeteringDataLoader;
import kz.kegoc.bln.entity.media.raw.DayMeteringDataRaw;

@Singleton
@Startup
public class DayMeteringDataRawLoader
	extends AbstractMeteringDataRawLoader<DayMeteringDataRaw>
		implements MeteringDataLoader {

	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		super.execute();
	}
}
