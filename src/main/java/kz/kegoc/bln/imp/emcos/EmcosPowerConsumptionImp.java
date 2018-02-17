package kz.kegoc.bln.imp.emcos;

import kz.kegoc.bln.ejb.interceptor.ProducerMonitor;
import kz.kegoc.bln.entity.data.PowerConsumptionRaw;
import kz.kegoc.bln.imp.Importer;
import kz.kegoc.bln.imp.emcos.reader.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
public class EmcosPowerConsumptionImp implements Importer {
	private static final Logger logger = LoggerFactory.getLogger(EmcosPowerConsumptionImp.class);
	
	@ProducerMonitor
	@Schedule(minute = "*/60", hour = "*", persistent = false)
	public void execute() {
		try {
			reader.read();
		}
		
		catch (Exception e) {
			logger.error("EmcosPowerConsumptionImp.execute failed: " + e.getMessage());
		}
    }

	@Inject
	private Reader<PowerConsumptionRaw> reader;
}
