package kz.kegoc.bln.producer.file;

import kz.kegoc.bln.entity.media.MeteringData;
import kz.kegoc.bln.entity.media.day.DayMeteringDataRaw;
import kz.kegoc.bln.entity.media.hour.HourMeteringDataRaw;
import kz.kegoc.bln.entity.media.month.MonthMeteringDataRaw;
import kz.kegoc.bln.ejb.interceptor.ProducerMonitor;
import kz.kegoc.bln.producer.MeteringDataProducer;
import kz.kegoc.bln.ejb.annotation.CSV;
import kz.kegoc.bln.ejb.annotation.XML;
import kz.kegoc.bln.producer.file.reader.FileMeteringDataReader;
import org.apache.commons.io.FilenameUtils;

import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Startup
@Singleton
public class FileMeteringDataRawProducer<T extends MeteringData> implements MeteringDataProducer {
	private String dir = "/home/eugene/dev/src/IdeaProjects/data";
	private Map<String, FileMeteringDataReader<? extends MeteringData>> mapReaders = new HashMap<>();

	@PostConstruct
	public void init() {
		mapReaders.put("hour/csv", 	csvHourMeteringDataRawReader);
		mapReaders.put("hour/xml", 	xmlHourMeteringDataRawReader);
		mapReaders.put("day/csv", 	csvDayMeteringDataRawReader);
		mapReaders.put("day/xml", 	xmlDayMeteringDataRawReader);
		mapReaders.put("month/csv", 	csvMonthMeteringDataRawReader);
		mapReaders.put("month/xml", 	xmlMonthMeteringDataRawReader);
	}


	@ProducerMonitor
	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		Arrays.asList("hour", "day", "month").stream().forEach(subDir -> {
			for (Path p : getListFiles(Paths.get(dir + "/" + subDir))) {
				try {
					String extension = FilenameUtils.getExtension(p.toString());
					FileMeteringDataReader<? extends MeteringData> reader = mapReaders.get(subDir + "/" + extension);
					reader.loadFromFile(p);
					Files.delete(p);
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
			list = stream.collect(Collectors.toList());
		} 
		catch (IOException e) {
			list = Collections.emptyList();
		}
		return list;
	}

	@Inject @CSV
	private FileMeteringDataReader<HourMeteringDataRaw> csvHourMeteringDataRawReader;

	@Inject @XML
	private FileMeteringDataReader<HourMeteringDataRaw> xmlHourMeteringDataRawReader;

	@Inject @CSV
	private FileMeteringDataReader<DayMeteringDataRaw> csvDayMeteringDataRawReader;

	@Inject @XML
	private FileMeteringDataReader<DayMeteringDataRaw> xmlDayMeteringDataRawReader;

	@Inject @CSV
	private FileMeteringDataReader<MonthMeteringDataRaw> csvMonthMeteringDataRawReader;

	@Inject @XML
	private FileMeteringDataReader<MonthMeteringDataRaw> xmlMonthMeteringDataRawReader;
}
