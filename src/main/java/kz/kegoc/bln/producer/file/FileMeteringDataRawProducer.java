package kz.kegoc.bln.producer.file;

import kz.kegoc.bln.entity.media.MeteringData;
import kz.kegoc.bln.entity.media.DayMeteringDataRaw;
import kz.kegoc.bln.entity.media.HourMeteringDataRaw;
import kz.kegoc.bln.entity.media.MonthMeteringDataRaw;
import kz.kegoc.bln.producer.MeteringDataProducer;
import kz.kegoc.bln.annotation.CSV;
import kz.kegoc.bln.annotation.XML;
import kz.kegoc.bln.producer.file.reader.FileMeteringDataRawReader;
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
	private Map<String, FileMeteringDataRawReader<? extends MeteringData>> mapReaders = new HashMap<>();

	@PostConstruct
	public void init() {
		mapReaders.put("hour/csv", 	csvHourMeteringDataRawReader);
		mapReaders.put("hour/xml", 	xmlHourMeteringDataRawReader);
		mapReaders.put("day/csv", 	csvDayMeteringDataRawReader);
		mapReaders.put("day/xml", 	xmlDayMeteringDataRawReader);
		mapReaders.put("month/csv", 	csvMonthMeteringDataRawReader);
		mapReaders.put("month/xml", 	xmlMonthMeteringDataRawReader);
	}

	
	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		Arrays.asList("hour", "day", "month").stream().forEach(subDir -> {
			for (Path p : getListFiles(Paths.get(dir + "/" + subDir))) {
				try {
					String extension = FilenameUtils.getExtension(p.toString());
					FileMeteringDataRawReader<? extends MeteringData> reader = mapReaders.get(subDir + "/" + extension);
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
	private FileMeteringDataRawReader<HourMeteringDataRaw> csvHourMeteringDataRawReader;

	@Inject @XML
	private FileMeteringDataRawReader<HourMeteringDataRaw> xmlHourMeteringDataRawReader;

	@Inject @CSV
	private FileMeteringDataRawReader<DayMeteringDataRaw> csvDayMeteringDataRawReader;

	@Inject @XML
	private FileMeteringDataRawReader<DayMeteringDataRaw> xmlDayMeteringDataRawReader;

	@Inject @CSV
	private FileMeteringDataRawReader<MonthMeteringDataRaw> csvMonthMeteringDataRawReader;

	@Inject @XML
	private FileMeteringDataRawReader<MonthMeteringDataRaw> xmlMonthMeteringDataRawReader;
}