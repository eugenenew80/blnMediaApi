package kz.kegoc.bln.exp.ftp.schedule;

import kz.kegoc.bln.exp.ExportRunner;
import kz.kegoc.bln.exp.Sender;
import kz.kegoc.bln.imp.raw.PeriodTimeValueRaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.Singleton;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

@Singleton
public class PeriodTimeValueExp implements ExportRunner {
    private static final Logger logger = LoggerFactory.getLogger(PeriodTimeValueExp.class);

    //@ProducerMonitor
    //@Schedule(minute = "*/1", hour = "*", persistent = false)
    public void run() {
        try {
            sender.send();
        }

        catch (Exception e) {
            logger.error("run failed: " + e.getMessage());
        }
    }

    @Inject @Default
    private Sender<PeriodTimeValueRaw> sender;
}
