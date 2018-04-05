package kz.kegoc.bln.imp.emcos.schedule;

import kz.kegoc.bln.ejb.annotation.Auto;
import kz.kegoc.bln.ejb.annotation.Emcos;
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
public class AutoPeriodTimeValueImp implements ImportRunner {
	private static final Logger logger = LoggerFactory.getLogger(AutoPeriodTimeValueImp.class);
	
	@ProducerMonitor
	@Schedule(minute = "*/5", hour = "*", persistent = false)
	public void run() {
		try {
			reader.read();
		}
		
		catch (Exception e) {
			logger.error("run failed: " + e.getMessage());
		}
    }

	@Inject
	@Emcos @Auto
	private Reader<PeriodTimeValueRaw> reader;
}
