package kz.kegoc.bln.imp.emcos.schefule;

import kz.kegoc.bln.ejb.interceptor.ProducerMonitor;
import kz.kegoc.bln.entity.data.PowerConsumptionRaw;
import kz.kegoc.bln.imp.Importer;
import kz.kegoc.bln.imp.emcos.reader.auto.AutoReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
public class AutoPowerConsumptionImp implements Importer {
	private static final Logger logger = LoggerFactory.getLogger(AutoPowerConsumptionImp.class);
	
	@ProducerMonitor
	@Schedule(minute = "*/10", hour = "*", persistent = false)
	public void runImport() {
		try {
			reader.read();
		}
		
		catch (Exception e) {
			logger.error("AutoPowerConsumptionImp.runImport failed: " + e.getMessage());
		}
    }

	@Inject
	private AutoReader<PowerConsumptionRaw> reader;
}
