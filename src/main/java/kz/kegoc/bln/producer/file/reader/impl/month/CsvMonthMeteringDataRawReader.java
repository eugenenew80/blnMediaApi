package kz.kegoc.bln.producer.file.reader.impl.month;

import kz.kegoc.bln.entity.media.MonthMeteringDataRaw;
import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.producer.file.reader.FileMeteringDataRawReader;
import kz.kegoc.bln.queue.MeteringDataQueueService;
import kz.kegoc.bln.annotation.CSV;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Stateless
@CSV
public class CsvMonthMeteringDataRawReader implements FileMeteringDataRawReader<MonthMeteringDataRaw> {

	@Inject
	public CsvMonthMeteringDataRawReader(MeteringDataQueueService<MonthMeteringDataRaw> service) {
		this.service=service;
	}

	public void loadFromFile(Path path) throws Exception {
		List<MonthMeteringDataRaw> list = new ArrayList<>();
		List<String> strs = Files.readAllLines(path);
		for (int i=1; i<strs.size(); i++ ) {
			list.add(convert(strs.get(i)));
		}

		service.addAll(list);
	}
	
	
	private MonthMeteringDataRaw convert(String s) {
		String[] data = s.split(";");
		MonthMeteringDataRaw d = new MonthMeteringDataRaw();
		d.setYear( Short.parseShort(data[0]));
		d.setMonth( Short.parseShort(data[1]));
		d.setExternalCode(data[2]);
		d.setParamCode(data[3]);
		d.setUnitCode(data[4]);
		d.setVal( Double.parseDouble(data[5]) );
		d.setWayEntering(WayEntering.CSV);
		d.setStatus(DataStatus.RAW);
		d.setDataSourceCode("MANUAL");

		return d;
	}

	private MeteringDataQueueService<MonthMeteringDataRaw> service;
}
