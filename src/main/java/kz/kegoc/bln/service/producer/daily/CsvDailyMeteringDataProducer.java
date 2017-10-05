package kz.kegoc.bln.service.producer.daily;

import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import kz.kegoc.bln.entity.media.DailyMeteringData;
import kz.kegoc.bln.entity.media.WayEnteringData;
import kz.kegoc.bln.entity.media.MeteringDataStatus;
import kz.kegoc.bln.service.producer.common.AbstractFileMeteringDataProducer;
import kz.kegoc.bln.service.producer.common.MeteringDataProducer;


@Singleton
@Startup
public class CsvDailyMeteringDataProducer extends AbstractFileMeteringDataProducer<DailyMeteringData> implements MeteringDataProducer {
    
    public CsvDailyMeteringDataProducer() {
		super("csv");
	}

    
	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		super.execute();
    }

	
	protected List<DailyMeteringData> loadFromFile(Path path) throws Exception {
		List<DailyMeteringData> list = new ArrayList<>();
		List<String> strs = Files.readAllLines(path);
		for (int i=1; i<strs.size(); i++ ) {
			list.add(convert(strs.get(i)));
		}
		return list;
	}
	
	
	private DailyMeteringData convert(String s) throws Exception {
		String[] data = s.split(";");
		
		DailyMeteringData d = new DailyMeteringData();
		d.setMeteringPointCode(data[1]);
		d.setParamCode(data[2]);
		d.setUnitCode(data[3]);
		d.setVal( Double.parseDouble(data[4]) );
		d.setWayEntering(WayEnteringData.CSV);
		d.setStatus(MeteringDataStatus.DRAFT);
		d.setDataSourceCode("MANUAL");
		
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-mm-dd");
		d.setMeteringDate(sd.parse(data[0]));
		return d;
	} 
}
