package kz.kegoc.bln.producer.file;

import com.google.common.collect.ImmutableList;
import kz.kegoc.bln.ejb.cdi.annotation.MeteringDataPath;
import kz.kegoc.bln.entity.media.Metering;
import kz.kegoc.bln.entity.media.raw.DayMeteringFlowRaw;
import kz.kegoc.bln.entity.media.raw.HourMeteringFlowRaw;
import kz.kegoc.bln.entity.media.raw.MonthMeteringFlowRaw;
import kz.kegoc.bln.ejb.interceptor.ProducerMonitor;
import kz.kegoc.bln.producer.MeteringDataProducer;
import kz.kegoc.bln.ejb.cdi.annotation.CSV;
import kz.kegoc.bln.ejb.cdi.annotation.XML;
import kz.kegoc.bln.producer.file.reader.FileMeteringReader;
import org.apache.commons.io.FilenameUtils;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.*;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Singleton
public class FileMeteringRawProducer implements MeteringDataProducer {
	private Map<String, FileMeteringReader<? extends Metering>> mapReaders = new HashMap<>();

	@PostConstruct
	public void init() {
		registerFileReader("hour/csv", 	csvHourMeteringDataRawReader);
		registerFileReader("hour/xml", 	xmlHourMeteringDataRawReader);
		registerFileReader("day/csv", 	csvDayMeteringDataRawReader);
		registerFileReader("day/xml", 	xmlDayMeteringDataRawReader);
		registerFileReader("month/csv", 	csvMonthMeteringDataRawReader);
		registerFileReader("month/csv", 	csvMonthMeteringDataRawReader);

	}


	public void registerFileReader(String subDir, FileMeteringReader<? extends Metering> reader) {
		mapReaders.put(subDir, 	reader);
	}


	@ProducerMonitor
	@Schedule(minute = "*/1", hour = "*", persistent = false)
	public void execute() {
		ImmutableList.of("hour", "day", "month").stream().forEach(subDir -> {
			for (Path p : getListFiles(Paths.get(dir + "/" + subDir))) {
				try {
					String fileExtension = FilenameUtils.getExtension(p.toString());
					FileMeteringReader<? extends Metering> reader = mapReaders.get(subDir + "/" + fileExtension);
					if (reader!=null) {
						reader.read(p);
						Files.delete(p);
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    }
    
	
	private List<Path> getListFiles(Path path) {
		List<Path> list;
		try (Stream<Path> stream = Files.list(path))  {
			list = stream.collect(toList());
		} 
		catch (IOException e) {
			list = emptyList();
		}
		return list;
	}


	@Inject @CSV
	private FileMeteringReader<HourMeteringFlowRaw> csvHourMeteringDataRawReader;

	@Inject @XML
	private FileMeteringReader<HourMeteringFlowRaw> xmlHourMeteringDataRawReader;

	@Inject @CSV
	private FileMeteringReader<DayMeteringFlowRaw> csvDayMeteringDataRawReader;

	@Inject @XML
	private FileMeteringReader<DayMeteringFlowRaw> xmlDayMeteringDataRawReader;

	@Inject @CSV
	private FileMeteringReader<MonthMeteringFlowRaw> csvMonthMeteringDataRawReader;

	@Inject @XML
	private FileMeteringReader<MonthMeteringFlowRaw> xmlMonthMeteringDataRawReader;

	@Inject @MeteringDataPath
	private String dir;
}
