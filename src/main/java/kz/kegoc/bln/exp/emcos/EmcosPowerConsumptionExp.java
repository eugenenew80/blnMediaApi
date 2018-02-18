package kz.kegoc.bln.exp.emcos;

import kz.kegoc.bln.ejb.interceptor.ProducerMonitor;
import kz.kegoc.bln.entity.data.PowerConsumption;
import kz.kegoc.bln.exp.Exporter;
import kz.kegoc.bln.exp.emcos.sender.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
public class EmcosPowerConsumptionExp implements Exporter {
    private static final Logger logger = LoggerFactory.getLogger(EmcosPowerConsumptionExp.class);

    @ProducerMonitor
    @Schedule(minute = "*/5", hour = "*", persistent = false)
    public void runExport() {
        try {
            sender.send();
        }

        catch (Exception e) {
            logger.error("EmcosPowerConsumptionExp.runExport failed: " + e.getMessage());
        }
    }

    @Inject
    private Sender<PowerConsumption> sender;
}
