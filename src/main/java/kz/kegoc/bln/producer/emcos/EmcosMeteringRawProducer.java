package kz.kegoc.bln.producer.emcos;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import kz.kegoc.bln.ejb.interceptor.ProducerMonitor;
import kz.kegoc.bln.entity.media.raw.DayMeteringBalanceRaw;
import kz.kegoc.bln.entity.media.raw.HourMeteringDataRaw;
import kz.kegoc.bln.producer.MeteringDataProducer;
import kz.kegoc.bln.producer.emcos.reader.EmcosMeteringReader;

@Singleton
public class EmcosMeteringRawProducer implements MeteringDataProducer {
	@ProducerMonitor
	@Schedule(minute = "*/30", hour = "*", persistent = false)
	public void execute() {
		emcosDayMeteringBalanceReader.read();
		emcosHourMeteringDataReader.read();
    }
	
	@Inject
	private EmcosMeteringReader<HourMeteringDataRaw> emcosHourMeteringDataReader;
	
	@Inject
	private EmcosMeteringReader<DayMeteringBalanceRaw> emcosDayMeteringBalanceReader;
}
