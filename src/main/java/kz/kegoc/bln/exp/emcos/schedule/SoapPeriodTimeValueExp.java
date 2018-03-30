package kz.kegoc.bln.exp.emcos.schedule;

import kz.kegoc.bln.ejb.annotation.SOAP;
import kz.kegoc.bln.ejb.interceptor.ProducerMonitor;
import kz.kegoc.bln.exp.ExportRunner;
import kz.kegoc.bln.exp.Sender;
import kz.kegoc.bln.imp.raw.PeriodTimeValueRaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
public class SoapPeriodTimeValueExp implements ExportRunner {
    private static final Logger logger = LoggerFactory.getLogger(SoapPeriodTimeValueExp.class);

    @ProducerMonitor
    @Schedule(minute = "*/1", hour = "*", persistent = false)
    public void run() {
        try {
            sender.send();
        }

        catch (Exception e) {
            logger.error("run failed: " + e.getMessage());
        }
    }

    @Inject @SOAP
    private Sender<PeriodTimeValueRaw> sender;
}
