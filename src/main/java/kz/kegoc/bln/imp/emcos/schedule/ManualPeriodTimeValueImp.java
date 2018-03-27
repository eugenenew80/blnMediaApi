package kz.kegoc.bln.imp.emcos.schedule;

import kz.kegoc.bln.ejb.cdi.annotation.Emcos;
import kz.kegoc.bln.ejb.cdi.annotation.Manual;
import kz.kegoc.bln.ejb.interceptor.ProducerMonitor;
import kz.kegoc.bln.imp.raw.PeriodTimeValueRaw;
import kz.kegoc.bln.imp.ImportRunner;
import kz.kegoc.bln.imp.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
public class ManualPeriodTimeValueImp implements ImportRunner {
	private static final Logger logger = LoggerFactory.getLogger(ManualPeriodTimeValueImp.class);
	
	@ProducerMonitor
	@Schedule(minute = "*/1", hour = "*", persistent = false)
	public void run() {
		try {
			reader.read();
		}
		
		catch (Exception e) {
			logger.error("ManualPeriodTimeValueImp.run failed: " + e.getMessage());
		}
    }

	@Inject
	@Emcos @Manual
	private Reader<PeriodTimeValueRaw> reader;
}
