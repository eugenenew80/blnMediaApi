package kz.kegoc.bln.producer.raw.daily;

import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import kz.kegoc.bln.entity.media.DailyMeteringDataRaw;
import kz.kegoc.bln.entity.media.WayEnteringData;
import kz.kegoc.bln.entity.media.MeteringDataStatus;
import kz.kegoc.bln.producer.common.AbstractFileMeteringDataProducer;
import kz.kegoc.bln.producer.common.MeteringDataProducer;


@Singleton
@Startup
public class CsvDailyMeteringDataRawProducer extends AbstractFileMeteringDataProducer<DailyMeteringDataRaw> implements MeteringDataProducer {
    
    public CsvDailyMeteringDataRawProducer() {
		super("daily/csv");
	}

    
	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		super.execute();
    }

	
	protected List<DailyMeteringDataRaw> loadFromFile(Path path) throws Exception {
		List<DailyMeteringDataRaw> list = new ArrayList<>();
		List<String> strs = Files.readAllLines(path);
		for (int i=1; i<strs.size(); i++ ) {
			list.add(convert(strs.get(i)));
		}
		return list;
	}
	
	
	private DailyMeteringDataRaw convert(String s) throws Exception {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-mm-dd");
		String[] data = s.split(";");

		DailyMeteringDataRaw d = new DailyMeteringDataRaw();
		d.setMeteringDate(sd.parse(data[0]));
		d.setMeteringPointCode(data[1]);
		d.setParamCode(data[2]);
		d.setUnitCode(data[3]);
		d.setVal( Double.parseDouble(data[4]) );
		d.setWayEntering(WayEnteringData.CSV);
		d.setStatus(MeteringDataStatus.DRAFT);
		d.setDataSourceCode("MANUAL");

		return d;
	} 
}
