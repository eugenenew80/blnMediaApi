package kz.kegoc.bln.producer.file.reader.impl;

import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.raw.MonthMeteringDataRaw;
import kz.kegoc.bln.producer.file.reader.FileMeteringReader;
import kz.kegoc.bln.queue.MeteringDataQueue;
import kz.kegoc.bln.ejb.cdi.annotation.CSV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Stateless
@CSV
public class CsvMonthMeteringDataRawReaderImpl implements FileMeteringReader<MonthMeteringDataRaw> {
	private static final Logger logger = LoggerFactory.getLogger(CsvMonthMeteringDataRawReaderImpl.class);

	@Inject
	public CsvMonthMeteringDataRawReaderImpl(MeteringDataQueue<MonthMeteringDataRaw> service) {
		this.queue =service;
	}

	public void read(Path path)  {
		logger.info("CsvMonthMeteringDataRawReaderImpl.read started");
		logger.info("file: " + path.toString());

		List<String> strs = readFile(path);

		logger.debug("Parsing file content started");
		List<MonthMeteringDataRaw> list = IntStream.range(1, strs.size())
			.mapToObj(i -> strs.get(i))
			.map(this::convert)
			.collect(toList());
		logger.info("Parsing file content completed, count of rows: " + list.size());

		logger.debug("Adding data to queue");
		queue.addAll(list);
		logger.info("CsvMonthMeteringDataRawReaderImpl.read completed");
	}

	private List<String> readFile(Path path) {
		logger.debug("Reading file content started");
		List<String> strs;
		try {
			strs = Files.readAllLines(path);
		}
		catch (IOException e) {
			logger.error("Reading file content failed: " + e.getMessage());
			strs = emptyList();
		}
		logger.debug("Reading file content competed");

		return strs;
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

	private MeteringDataQueue<MonthMeteringDataRaw> queue;
}
