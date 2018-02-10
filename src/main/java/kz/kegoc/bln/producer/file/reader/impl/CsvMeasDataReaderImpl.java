package kz.kegoc.bln.producer.file.reader.impl;

import kz.kegoc.bln.entity.common.InputMethod;
import kz.kegoc.bln.entity.common.ReceivingMethod;
import kz.kegoc.bln.entity.common.SourceSystem;
import kz.kegoc.bln.entity.data.MeasDataRaw;
import kz.kegoc.bln.producer.file.reader.FileMeteringDataReader;
import kz.kegoc.bln.queue.MeteringDataQueue;
import kz.kegoc.bln.ejb.cdi.annotation.CSV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Stateless @CSV
public class CsvMeasDataReaderImpl implements FileMeteringDataReader<MeasDataRaw> {
	private static final Logger logger = LoggerFactory.getLogger(CsvMeasDataReaderImpl.class);

	@Inject
	public CsvMeasDataReaderImpl(MeteringDataQueue<MeasDataRaw> service) {
		this.queue =service;
	}

	public void read(Path path)  {
		logger.info("CsvMeasDataReaderImpl.read started");
		logger.info("file: " + path.toString());

		List<String> strs = readFile(path);

		logger.debug("Parsing file content started");
		List<MeasDataRaw> list = IntStream.range(1, strs.size())
			.mapToObj(i -> strs.get(i))
			.map(this::convert)
			.collect(toList());
		logger.info("Parsing file content completed, count of rows: " + list.size());

		logger.debug("Adding data to queue");
		queue.addAll(list);
		logger.info("CsvMeasDataReaderImpl.read completed");
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

	private MeasDataRaw convert(String s) {
		String[] data = s.split(";");
		MeasDataRaw d = new MeasDataRaw();
		d.setMeasDate(LocalDateTime.parse(data[0], DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")));
		d.setSourceMeteringPointCode(data[2]);
		d.setSourceParamCode(data[3]);
		d.setSourceUnitCode(data[4]);
		d.setVal( Double.parseDouble(data[5]) );
		d.setSourceSystemCode(SourceSystem.NOT_SET);
		d.setInputMethod(InputMethod.FILE);
		d.setReceivingMethod(ReceivingMethod.NOT_SET);

		return d;
	}

	private MeteringDataQueue<MeasDataRaw> queue;
}
