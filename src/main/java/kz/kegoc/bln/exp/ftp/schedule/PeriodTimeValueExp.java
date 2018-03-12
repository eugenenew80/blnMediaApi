package kz.kegoc.bln.exp.ftp.schedule;

import kz.kegoc.bln.ejb.interceptor.ProducerMonitor;
import kz.kegoc.bln.entity.data.PeriodTimeValue;
import kz.kegoc.bln.exp.Exporter;
import kz.kegoc.bln.exp.ftp.sender.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
public class PeriodTimeValueExp implements Exporter {
    private static final Logger logger = LoggerFactory.getLogger(PeriodTimeValueExp.class);

    @ProducerMonitor
    @Schedule(minute = "*/1", hour = "*", persistent = false)
    public void runExport() {
        try {
            sender.send();
        }

        catch (Exception e) {
            logger.error("PeriodTimeValueExp.runExport failed: " + e.getMessage());
        }
    }

    @Inject
    private Sender<PeriodTimeValue> sender;
}
