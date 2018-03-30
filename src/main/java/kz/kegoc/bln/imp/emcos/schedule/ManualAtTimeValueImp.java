package kz.kegoc.bln.imp.emcos.schedule;

import kz.kegoc.bln.ejb.annotation.Emcos;
import kz.kegoc.bln.ejb.annotation.Manual;
import kz.kegoc.bln.ejb.interceptor.ProducerMonitor;
import kz.kegoc.bln.imp.raw.AtTimeValueRaw;
import kz.kegoc.bln.imp.ImportRunner;
import kz.kegoc.bln.imp.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
public class ManualAtTimeValueImp implements ImportRunner {
	private static final Logger logger = LoggerFactory.getLogger(ManualAtTimeValueImp.class);
	
	@ProducerMonitor
	@Schedule(minute = "*/1", hour = "*", persistent = false)
	public void run() {
		try {
			reader.read();
		}
		
		catch (Exception e) {
			logger.error("run failed: " + e.getMessage());
		}
    }

	@Inject
	@Emcos @Manual
	private Reader<AtTimeValueRaw> reader;
}
