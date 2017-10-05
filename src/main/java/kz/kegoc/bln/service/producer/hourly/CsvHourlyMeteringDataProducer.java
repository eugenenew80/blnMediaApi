package kz.kegoc.bln.service.producer.hourly;

import kz.kegoc.bln.entity.media.HourlyMeteringData;
import kz.kegoc.bln.entity.media.MeteringDataStatus;
import kz.kegoc.bln.entity.media.WayEnteringData;
import kz.kegoc.bln.service.producer.common.AbstractFileMeteringDataProducer;
import kz.kegoc.bln.service.producer.common.MeteringDataProducer;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


@Singleton
@Startup
public class CsvHourlyMeteringDataProducer extends AbstractFileMeteringDataProducer<HourlyMeteringData> implements MeteringDataProducer {

    public CsvHourlyMeteringDataProducer() {
		super("csv");
	}

    
	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		super.execute();
    }

	
	protected List<HourlyMeteringData> loadFromFile(Path path) throws Exception {
		List<HourlyMeteringData> list = new ArrayList<>();
		List<String> strs = Files.readAllLines(path);
		for (int i=1; i<strs.size(); i++ ) {
			list.add(convert(strs.get(i)));
		}
		return list;
	}
	
	
	private HourlyMeteringData convert(String s) throws Exception {
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-mm-dd");

		String[] data = s.split(";");
		HourlyMeteringData d = new HourlyMeteringData();
		d.setMeteringDate(sd.parse(data[0]));
		d.setHour( Byte.parseByte(data[1]));
		d.setMeteringPointCode(data[2]);
		d.setParamCode(data[3]);
		d.setUnitCode(data[4]);
		d.setVal( Double.parseDouble(data[5]) );
		d.setWayEntering(WayEnteringData.CSV);
		d.setStatus(MeteringDataStatus.DRAFT);
		d.setDataSourceCode("MANUAL");

		return d;
	} 
}
