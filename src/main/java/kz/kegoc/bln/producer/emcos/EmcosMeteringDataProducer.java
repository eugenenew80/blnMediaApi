package kz.kegoc.bln.producer.emcos;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import kz.kegoc.bln.entity.data.MeteringReading;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import kz.kegoc.bln.ejb.interceptor.ProducerMonitor;
import kz.kegoc.bln.entity.data.MeasData;
import kz.kegoc.bln.producer.MeteringDataProducer;
import kz.kegoc.bln.producer.emcos.reader.EmcosMeteringDataReader;

@Singleton
public class EmcosMeteringDataProducer implements MeteringDataProducer {
	private static final Logger logger = LoggerFactory.getLogger(EmcosMeteringDataProducer.class);
	
	@ProducerMonitor
	@Schedule(minute = "*/30", hour = "*", persistent = false)
	public void execute() {
		try {
			emcosMeteringReadingReader.read();
			emcosMeasDataReader.read();
		}
		
		catch (Exception e) {
			logger.error("EmcosMeteringDataProducer.execute failed: " + e.getMessage());
		}
    }

	@Inject
	private EmcosMeteringDataReader<MeasData> emcosMeasDataReader;
	
	@Inject
	private EmcosMeteringDataReader<MeteringReading> emcosMeteringReadingReader;
}
