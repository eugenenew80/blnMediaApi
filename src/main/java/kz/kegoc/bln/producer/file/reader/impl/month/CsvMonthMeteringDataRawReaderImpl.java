package kz.kegoc.bln.producer.file.reader.impl.month;

import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.month.MonthMeteringDataRaw;
import kz.kegoc.bln.producer.file.reader.FileMeteringReader;
import kz.kegoc.bln.queue.MeteringDataQueue;
import kz.kegoc.bln.ejb.annotation.CSV;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Stateless
@CSV
public class CsvMonthMeteringDataRawReaderImpl implements FileMeteringReader<MonthMeteringDataRaw> {

	@Inject
	public CsvMonthMeteringDataRawReaderImpl(MeteringDataQueue<MonthMeteringDataRaw> service) {
		this.service=service;
	}

	public void read(Path path)  {
		List<String> strs = null;
		try {
			strs = Files.readAllLines(path);
		}
		catch (IOException e) {
			e.printStackTrace();
			strs = emptyList();
		}

		List<MonthMeteringDataRaw> list = strs.stream()
			.map(this::convert)
			.collect(toList());

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

	private MeteringDataQueue<MonthMeteringDataRaw> service;
}
