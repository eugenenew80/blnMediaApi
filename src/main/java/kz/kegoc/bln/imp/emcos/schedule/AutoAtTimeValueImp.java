package kz.kegoc.bln.imp.emcos.schedule;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import kz.kegoc.bln.ejb.annotation.Auto;
import kz.kegoc.bln.ejb.annotation.Emcos;
import kz.kegoc.bln.imp.raw.AtTimeValueRaw;
import kz.kegoc.bln.imp.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import kz.kegoc.bln.ejb.interceptor.ProducerMonitor;
import kz.kegoc.bln.imp.ImportRunner;

@Singleton
public class AutoAtTimeValueImp implements ImportRunner {
	private static final Logger logger = LoggerFactory.getLogger(AutoAtTimeValueImp.class);

	@ProducerMonitor
	@Schedule(minute = "05", hour = "*/1", persistent = false)
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
	private Reader<AtTimeValueRaw> reader;
}
