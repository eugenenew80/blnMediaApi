package kz.kegoc.bln.imp.emcos;

import kz.kegoc.bln.ejb.interceptor.ProducerMonitor;
import kz.kegoc.bln.imp.Importer;
import kz.kegoc.bln.imp.emcos.manual.PowerConsumptionReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
public class ManualPowerConsumptionImp implements Importer {
	private static final Logger logger = LoggerFactory.getLogger(ManualPowerConsumptionImp.class);
	
	@ProducerMonitor
	@Schedule(minute = "*/1", hour = "*", persistent = false)
	public void runImport() {
		try {
			reader.read();
		}
		
		catch (Exception e) {
			logger.error("ManualPowerConsumptionImp.runImport failed: " + e.getMessage());
		}
    }

	@Inject
	private PowerConsumptionReader reader;
}
