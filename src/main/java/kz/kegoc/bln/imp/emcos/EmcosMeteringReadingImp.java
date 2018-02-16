package kz.kegoc.bln.producer.emcos;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import kz.kegoc.bln.entity.data.MeteringReadingRaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import kz.kegoc.bln.ejb.interceptor.ProducerMonitor;
import kz.kegoc.bln.producer.DataProducer;
import kz.kegoc.bln.producer.emcos.reader.DataReader;

@Singleton
public class EmcosMeteringReadingProducer implements DataProducer {
	private static final Logger logger = LoggerFactory.getLogger(EmcosMeteringReadingProducer.class);
	
	@ProducerMonitor
	@Schedule(minute = "*/5", hour = "*", persistent = false)
	public void execute() {
		try {
			reader.read();
		}
		
		catch (Exception e) {
			logger.error("EmcosMeteringReadingProducer.execute failed: " + e.getMessage());
		}
    }


	@Inject
	private DataReader<MeteringReadingRaw> reader;
}
