package kz.kegoc.bln.producer.emcos;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import kz.kegoc.bln.entity.data.MeasDataRaw;
import kz.kegoc.bln.entity.data.MeteringReadingRaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import kz.kegoc.bln.ejb.interceptor.ProducerMonitor;
import kz.kegoc.bln.producer.DataProducer;
import kz.kegoc.bln.producer.emcos.reader.EmcosMeteringDataReader;

@Singleton
public class EmcosMeteringDataProducer implements DataProducer {
	private static final Logger logger = LoggerFactory.getLogger(EmcosMeteringDataProducer.class);
	
	@ProducerMonitor
	@Schedule(minute = "*/1", hour = "*", persistent = false)
	public void execute() {
		try {
			emcosMeteringReadingReader.read();
			//emcosMeasDataReader.read();
		}
		
		catch (Exception e) {
			logger.error("EmcosMeteringDataProducer.execute failed: " + e.getMessage());
		}
    }

	@Inject
	private EmcosMeteringDataReader<MeasDataRaw> emcosMeasDataReader;
	
	@Inject
	private EmcosMeteringDataReader<MeteringReadingRaw> emcosMeteringReadingReader;
}
