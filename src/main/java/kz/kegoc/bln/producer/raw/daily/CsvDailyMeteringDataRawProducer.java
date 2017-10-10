package kz.kegoc.bln.producer.raw.daily;

import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import kz.kegoc.bln.entity.media.raw.DayMeteringDataRaw;
import kz.kegoc.bln.entity.media.WayEnteringData;
import kz.kegoc.bln.entity.media.MeteringDataStatus;
import kz.kegoc.bln.producer.common.AbstractFileMeteringDataProducer;
import kz.kegoc.bln.producer.common.MeteringDataProducer;


@Singleton
@Startup
public class CsvDailyMeteringDataRawProducer extends AbstractFileMeteringDataProducer<DayMeteringDataRaw> implements MeteringDataProducer {
    
    public CsvDailyMeteringDataRawProducer() {
		super("daily/csv");
	}

	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		super.execute();
    }

	
	protected List<DayMeteringDataRaw> loadFromFile(Path path) throws Exception {
		List<DayMeteringDataRaw> list = new ArrayList<>();
		List<String> strs = Files.readAllLines(path);
		for (int i=1; i<strs.size(); i++ ) {
			list.add(convert(strs.get(i)));
		}
		return list;
	}
	
	
	private DayMeteringDataRaw convert(String s) throws Exception {
		String[] data = s.split(";");

		DayMeteringDataRaw d = new DayMeteringDataRaw();
		d.setMeteringDate(LocalDateTime.parse(data[0], DateTimeFormatter.ofPattern("yyyy-mm-dd")));
		d.setCode(data[1]);
		d.setParamCode(data[2]);
		d.setUnitCode(data[3]);
		d.setVal( Double.parseDouble(data[4]) );
		d.setWayEntering(WayEnteringData.CSV);
		d.setStatus(MeteringDataStatus.DRAFT);
		d.setDataSourceCode("MANUAL");

		return d;
	} 
}
