package kz.kegoc.bln.imp.emcos.schefule;

import kz.kegoc.bln.ejb.interceptor.ProducerMonitor;
import kz.kegoc.bln.entity.data.MeteringReadingRaw;
import kz.kegoc.bln.imp.Importer;
import kz.kegoc.bln.imp.emcos.reader.manual.ManualReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
public class ManualMeteringReadingImp implements Importer {
	private static final Logger logger = LoggerFactory.getLogger(ManualMeteringReadingImp.class);
	
	@ProducerMonitor
	@Schedule(minute = "*/1", hour = "*", persistent = false)
	public void runImport() {
		try {
			reader.read();
		}
		
		catch (Exception e) {
			logger.error("ManualMeteringReadingImp.runImport failed: " + e.getMessage());
		}
    }

	@Inject
	private ManualReader<MeteringReadingRaw> reader;
}
