package kz.kegoc.bln.producer.emcos;

import kz.kegoc.bln.ejb.interceptor.ProducerMonitor;
import kz.kegoc.bln.entity.data.PowerConsumptionRaw;
import kz.kegoc.bln.producer.DataProducer;
import kz.kegoc.bln.producer.emcos.reader.DataReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
public class EmcosPowerConsumptionProducer implements DataProducer {
	private static final Logger logger = LoggerFactory.getLogger(EmcosPowerConsumptionProducer.class);
	
	@ProducerMonitor
	@Schedule(minute = "*/5", hour = "*", persistent = false)
	public void execute() {
		try {
			reader.read();
		}
		
		catch (Exception e) {
			logger.error("EmcosPowerConsumptionProducer.execute failed: " + e.getMessage());
		}
    }

	@Inject
	private DataReader<PowerConsumptionRaw> reader;
}
