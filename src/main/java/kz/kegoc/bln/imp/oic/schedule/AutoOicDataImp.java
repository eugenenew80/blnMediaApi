package kz.kegoc.bln.imp.oic.schedule;

import kz.kegoc.bln.gateway.oic.OicDataImpGateway;
import kz.kegoc.bln.gateway.oic.impl.OicDataImpGatewayImpl;
import kz.kegoc.bln.imp.Importer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

@Singleton
public class AutoOicDataImp implements Importer {
	private static final Logger logger = LoggerFactory.getLogger(AutoOicDataImp.class);

	@Schedule(minute = "*/5", hour = "*", persistent = false)
	public void runImport() {
		try {
			OicDataImpGateway gateway = new OicDataImpGatewayImpl();
			gateway.request();
		}
		
		catch (Exception e) {
			logger.error("AutoOicDataImp.runImport failed: " + e.getMessage());
		}
    }
}
