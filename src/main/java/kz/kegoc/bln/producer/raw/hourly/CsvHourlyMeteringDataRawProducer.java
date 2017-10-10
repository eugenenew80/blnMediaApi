package kz.kegoc.bln.producer.raw.hourly;

import kz.kegoc.bln.entity.media.raw.HourMeteringDataRaw;
import kz.kegoc.bln.entity.media.MeteringDataStatus;
import kz.kegoc.bln.entity.media.WayEnteringData;
import kz.kegoc.bln.producer.common.AbstractFileMeteringDataProducer;
import kz.kegoc.bln.producer.common.MeteringDataProducer;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Singleton
@Startup
public class CsvHourlyMeteringDataRawProducer extends AbstractFileMeteringDataProducer<HourMeteringDataRaw> implements MeteringDataProducer {

    public CsvHourlyMeteringDataRawProducer() {
		super("hourly/csv");
	}

    
	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		super.execute();
    }

	
	protected List<HourMeteringDataRaw> loadFromFile(Path path) throws Exception {
		List<HourMeteringDataRaw> list = new ArrayList<>();
		List<String> strs = Files.readAllLines(path);
		for (int i=1; i<strs.size(); i++ ) {
			list.add(convert(strs.get(i)));
		}
		return list;
	}
	
	
	private HourMeteringDataRaw convert(String s) throws Exception {
		String[] data = s.split(";");
		HourMeteringDataRaw d = new HourMeteringDataRaw();
		d.setMeteringDate(LocalDateTime.parse(data[0], DateTimeFormatter.ofPattern("yyyy-mm-dd")));
		d.setHour( Byte.parseByte(data[1]));
		d.setCode(data[2]);
		d.setParamCode(data[3]);
		d.setUnitCode(data[4]);
		d.setVal( Double.parseDouble(data[5]) );
		d.setWayEntering(WayEnteringData.CSV);
		d.setStatus(MeteringDataStatus.DRAFT);
		d.setDataSourceCode("MANUAL");

		return d;
	} 
}
