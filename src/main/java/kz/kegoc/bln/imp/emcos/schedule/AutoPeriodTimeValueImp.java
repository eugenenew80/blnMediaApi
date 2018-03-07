package kz.kegoc.bln.imp.emcos.schedule;

import kz.kegoc.bln.ejb.interceptor.ProducerMonitor;
import kz.kegoc.bln.entity.data.PeriodTimeValueRaw;
import kz.kegoc.bln.imp.Importer;
import kz.kegoc.bln.imp.emcos.reader.auto.AutoReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
public class AutoPeriodTimeValueImp implements Importer {
	private static final Logger logger = LoggerFactory.getLogger(AutoPeriodTimeValueImp.class);
	
	@ProducerMonitor
	@Schedule(minute = "*/30", hour = "*", persistent = false)
	public void runImport() {
		try {
			reader.read();
		}
		
		catch (Exception e) {
			logger.error("AutoPeriodTimeValueImp.runImport failed: " + e.getMessage());
		}
    }

	@Inject
	private AutoReader<PeriodTimeValueRaw> reader;
}
