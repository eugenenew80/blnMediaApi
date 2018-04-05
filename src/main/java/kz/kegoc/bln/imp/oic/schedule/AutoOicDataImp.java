package kz.kegoc.bln.imp.oic.schedule;

import kz.kegoc.bln.ejb.annotation.Auto;
import kz.kegoc.bln.ejb.annotation.Oic;
import kz.kegoc.bln.ejb.interceptor.ProducerMonitor;
import kz.kegoc.bln.imp.ImportRunner;
import kz.kegoc.bln.imp.Reader;
import kz.kegoc.bln.imp.raw.TelemetryRaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
public class AutoOicDataImp implements ImportRunner {
	private static final Logger logger = LoggerFactory.getLogger(AutoOicDataImp.class);

	@ProducerMonitor
	@Schedule(minute = "*/15", hour = "*", persistent = false)
	public void run() {
		try {
			reader.read();
		}

		catch (Exception e) {
			logger.error("run failed: " + e.getMessage());
		}
	}

	@Inject
	@Oic @Auto
	private Reader<TelemetryRaw> reader;
}
