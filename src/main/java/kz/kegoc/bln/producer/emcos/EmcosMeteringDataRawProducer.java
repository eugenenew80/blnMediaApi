package kz.kegoc.bln.producer.emcos;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import kz.kegoc.bln.ejb.interceptor.ProducerMonitor;
import kz.kegoc.bln.entity.media.day.DayMeteringBalanceRaw;
import kz.kegoc.bln.entity.media.hour.HourMeteringDataRaw;
import kz.kegoc.bln.producer.MeteringDataProducer;
import kz.kegoc.bln.producer.emcos.reader.EmcosMeteringDataReader;

@Singleton
public class EmcosMeteringDataRawProducer implements MeteringDataProducer {
	
	@ProducerMonitor
	@Schedule(minute = "*/5", hour = "*", persistent = false)
	public void execute() {
		emcosDayMeteringBalanceReader.loadFromEmcos();
		emcosHourMeteringDataReader.loadFromEmcos();
    }
	
	@Inject
	private EmcosMeteringDataReader<HourMeteringDataRaw> emcosHourMeteringDataReader;
	
	@Inject
	private EmcosMeteringDataReader<DayMeteringBalanceRaw> emcosDayMeteringBalanceReader;
}
