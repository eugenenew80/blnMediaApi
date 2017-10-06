package kz.kegoc.bln.producer.raw.monthly;

import kz.kegoc.bln.entity.media.MonthlyMeteringDataRaw;
import kz.kegoc.bln.entity.media.MeteringDataStatus;
import kz.kegoc.bln.entity.media.WayEnteringData;
import kz.kegoc.bln.producer.common.AbstractFileMeteringDataProducer;
import kz.kegoc.bln.producer.common.MeteringDataProducer;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


@Singleton
@Startup
public class CsvMonthlyMeteringDataRawProducer extends AbstractFileMeteringDataProducer<MonthlyMeteringDataRaw> implements MeteringDataProducer {

    public CsvMonthlyMeteringDataRawProducer() {
		super("monthly/csv");
	}

    
	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		super.execute();
    }

	
	protected List<MonthlyMeteringDataRaw> loadFromFile(Path path) throws Exception {
		List<MonthlyMeteringDataRaw> list = new ArrayList<>();
		List<String> strs = Files.readAllLines(path);
		for (int i=1; i<strs.size(); i++ ) {
			list.add(convert(strs.get(i)));
		}
		return list;
	}
	
	
	private MonthlyMeteringDataRaw convert(String s) {
		String[] data = s.split(";");
		MonthlyMeteringDataRaw d = new MonthlyMeteringDataRaw();
		d.setMeteringYear( Short.parseShort(data[0]));
		d.setMeteringMonth( Short.parseShort(data[1]));
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
