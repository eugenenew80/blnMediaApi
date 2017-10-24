package kz.kegoc.bln.producer.file.reader.impl.hour;

import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.hour.HourMeteringDataRaw;
import kz.kegoc.bln.producer.file.reader.FileMeteringReader;
import kz.kegoc.bln.queue.MeteringDataQueue;
import kz.kegoc.bln.ejb.annotation.CSV;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Stateless @CSV
public class CsvHourMeteringDataRawReaderImpl implements FileMeteringReader<HourMeteringDataRaw> {

	@Inject
	public CsvHourMeteringDataRawReaderImpl(MeteringDataQueue<HourMeteringDataRaw> service) {
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

		List<HourMeteringDataRaw> list = strs.stream()
			.map(this::convert)
			.collect(toList());

		service.addAll(list);
	}
	
	
	private HourMeteringDataRaw convert(String s) {
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

	private MeteringDataQueue<HourMeteringDataRaw> service;
}
