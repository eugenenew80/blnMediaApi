package kz.kegoc.bln.imp.emcos;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import kz.kegoc.bln.entity.data.MeteringReadingRaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import kz.kegoc.bln.ejb.interceptor.ProducerMonitor;
import kz.kegoc.bln.imp.Importer;
import kz.kegoc.bln.imp.emcos.reader.Reader;

@Singleton
public class EmcosMeteringReadingImp implements Importer {
	private static final Logger logger = LoggerFactory.getLogger(EmcosMeteringReadingImp.class);
	
	@ProducerMonitor
	@Schedule(minute = "30/60", hour = "*", persistent = false)
	public void execute() {
		try {
			reader.read();
		}
		
		catch (Exception e) {
			logger.error("EmcosMeteringReadingImp.execute failed: " + e.getMessage());
		}
    }


	@Inject
	private Reader<MeteringReadingRaw> reader;
}
