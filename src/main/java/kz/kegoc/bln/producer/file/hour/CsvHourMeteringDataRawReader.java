package kz.kegoc.bln.producer.file.hour;

import kz.kegoc.bln.entity.media.raw.HourMeteringDataRaw;
import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.producer.file.FileMeteringDataRawReader;
import kz.kegoc.bln.queue.MeteringDataQueueService;
import kz.kegoc.bln.annotation.CSV;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Stateless @CSV
public class CsvHourMeteringDataRawReader implements FileMeteringDataRawReader<HourMeteringDataRaw> {

	@Inject
	public CsvHourMeteringDataRawReader(MeteringDataQueueService<HourMeteringDataRaw> service) {
		this.service=service;
	}

	public void loadFromFile(Path path) throws Exception {
		List<HourMeteringDataRaw> list = new ArrayList<>();
		List<String> strs = Files.readAllLines(path);
		for (int i=1; i<strs.size(); i++ )
			list.add(convert(strs.get(i)));

		service.addAll(list);
	}
	
	
	private HourMeteringDataRaw convert(String s) throws Exception {
		String[] data = s.split(";");
		HourMeteringDataRaw d = new HourMeteringDataRaw();
		d.setMeteringDate(LocalDate.parse(data[0], DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		d.setHour( Integer.parseInt(data[1]));
		d.setExternalCode(data[2]);
		d.setParamCode(data[3]);
		d.setUnitCode(data[4]);
		d.setVal( Double.parseDouble(data[5]) );
		d.setWayEntering(WayEntering.CSV);
		d.setStatus(DataStatus.RAW);
		d.setDataSourceCode("MANUAL");

		return d;
	}

	private MeteringDataQueueService<HourMeteringDataRaw> service;
}
