package kz.kegoc.bln.producer.emcos;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import kz.kegoc.bln.ejb.interceptor.ProducerMonitor;
import kz.kegoc.bln.entity.media.raw.DayMeteringBalanceRaw;
import kz.kegoc.bln.entity.media.raw.HourMeteringDataRaw;
import kz.kegoc.bln.producer.MeteringDataProducer;
import kz.kegoc.bln.producer.emcos.reader.EmcosMeteringReader;

@Singleton
public class EmcosMeteringRawProducer implements MeteringDataProducer {
	private static final Logger logger = LoggerFactory.getLogger(EmcosMeteringRawProducer.class);
	
	@ProducerMonitor
	@Schedule(minute = "*/30", hour = "*", persistent = false)
	public void execute() {
		try {
			emcosDayMeteringBalanceReader.read();
			emcosHourMeteringDataReader.read();
		}
		
		catch (Exception e) {
			logger.error("EmcosMeteringRawProducer.execute failed: " + e.getMessage());
		}
    }

	@Inject
	private EmcosMeteringReader<HourMeteringDataRaw> emcosHourMeteringDataReader;
	
	@Inject
	private EmcosMeteringReader<DayMeteringBalanceRaw> emcosDayMeteringBalanceReader;
}
