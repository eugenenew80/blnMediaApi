package kz.kegoc.bln.imp.emcos.schefule;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import kz.kegoc.bln.entity.data.AtTimeValueRaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import kz.kegoc.bln.ejb.interceptor.ProducerMonitor;
import kz.kegoc.bln.imp.Importer;
import kz.kegoc.bln.imp.emcos.reader.auto.AutoReader;

@Singleton
public class AutoAtTimeValueImp implements Importer {
	private static final Logger logger = LoggerFactory.getLogger(AutoAtTimeValueImp.class);
	
	@ProducerMonitor
	@Schedule(minute = "*/5", hour = "*", persistent = false)
	public void runImport() {
		try {
			reader.read();
		}
		
		catch (Exception e) {
			logger.error("AutoAtTimeValueImp.runImport failed: " + e.getMessage());
		}
    }

	@Inject
	private AutoReader<AtTimeValueRaw> reader;
}
