package kz.kegoc.bln.producer.file;

import com.google.common.collect.ImmutableList;
import kz.kegoc.bln.ejb.cdi.annotation.MeteringDataPath;
import kz.kegoc.bln.entity.media.Metering;
import kz.kegoc.bln.entity.media.data.DayMeteringFlow;
import kz.kegoc.bln.entity.media.data.HourMeteringFlow;
import kz.kegoc.bln.entity.media.data.MonthMeteringFlow;
import kz.kegoc.bln.ejb.interceptor.ProducerMonitor;
import kz.kegoc.bln.producer.MeteringDataProducer;
import kz.kegoc.bln.ejb.cdi.annotation.CSV;
import kz.kegoc.bln.ejb.cdi.annotation.XML;
import kz.kegoc.bln.producer.file.reader.FileMeteringDataReader;
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
public class FileMeteringDataProducer implements MeteringDataProducer {
	private Map<String, FileMeteringDataReader<? extends Metering>> mapReaders = new HashMap<>();

	@PostConstruct
	public void init() {
		registerFileReader("hour/csv", 	csvHourMeteringDataRawReader);
		registerFileReader("hour/xml", 	xmlHourMeteringDataRawReader);
		registerFileReader("day/csv", 	csvDayMeteringDataRawReader);
		registerFileReader("day/xml", 	xmlDayMeteringDataRawReader);
		registerFileReader("month/csv", 	csvMonthMeteringDataRawReader);
		registerFileReader("month/csv", 	csvMonthMeteringDataRawReader);

	}


	public void registerFileReader(String subDir, FileMeteringDataReader<? extends Metering> reader) {
		mapReaders.put(subDir, 	reader);
	}


	@ProducerMonitor
	@Schedule(minute = "*/1", hour = "*", persistent = false)
	public void execute() {
		ImmutableList.of("hour", "day", "month").stream().forEach(subDir -> {
			for (Path p : getListFiles(Paths.get(dir + "/" + subDir))) {
				try {
					String fileExtension = FilenameUtils.getExtension(p.toString());
					FileMeteringDataReader<? extends Metering> reader = mapReaders.get(subDir + "/" + fileExtension);
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
	private FileMeteringDataReader<HourMeteringFlow> csvHourMeteringDataRawReader;

	@Inject @XML
	private FileMeteringDataReader<HourMeteringFlow> xmlHourMeteringDataRawReader;

	@Inject @CSV
	private FileMeteringDataReader<DayMeteringFlow> csvDayMeteringDataRawReader;

	@Inject @XML
	private FileMeteringDataReader<DayMeteringFlow> xmlDayMeteringDataRawReader;

	@Inject @CSV
	private FileMeteringDataReader<MonthMeteringFlow> csvMonthMeteringDataRawReader;

	@Inject @XML
	private FileMeteringDataReader<MonthMeteringFlow> xmlMonthMeteringDataRawReader;

	@Inject @MeteringDataPath
	private String dir;
}