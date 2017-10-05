package kz.kegoc.bln.service.producer.daily.impl;

import java.nio.file.*;
import java.util.*;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import kz.kegoc.bln.entity.media.DailyMeteringData;
import kz.kegoc.bln.service.producer.common.MeteringDataProducer;
import kz.kegoc.bln.service.producer.daily.FileDailyMeteringDataProducer;


@Singleton
@Startup
public class XmlDailyMeteringDataProducer  extends FileDailyMeteringDataProducer implements MeteringDataProducer {
    
    public XmlDailyMeteringDataProducer() {
		super("xml");
	}

    
	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		super.execute();
    }

	
	protected List<DailyMeteringData> loadFromFile(Path path) throws Exception {
		List<DailyMeteringData> list = new ArrayList<>();
		return list;
	}
}
