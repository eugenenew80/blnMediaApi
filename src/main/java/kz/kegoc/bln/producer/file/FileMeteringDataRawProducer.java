package kz.kegoc.bln.producer.file;

import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.entity.media.raw.DayMeteringDataRaw;
import kz.kegoc.bln.entity.media.raw.HourMeteringDataRaw;
import kz.kegoc.bln.entity.media.raw.MonthMeteringDataRaw;
import kz.kegoc.bln.producer.common.MeteringDataProducer;
import kz.kegoc.bln.producer.file.day.CsvDayMeteringDataRawReader;
import kz.kegoc.bln.producer.file.day.XmlDayMeteringDataRawReader;
import kz.kegoc.bln.producer.file.hour.CsvHourMeteringDataRawReader;
import kz.kegoc.bln.producer.file.hour.XmlHourMeteringDataRawReader;
import kz.kegoc.bln.producer.file.month.CsvMonthMeteringDataRawReader;
import kz.kegoc.bln.producer.file.month.XmlMonthMeteringDataRawReader;
import kz.kegoc.bln.queue.common.MeteringDataQueueService;
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
public class FileMeteringDataRawProducer<T extends HasId> implements MeteringDataProducer {
	private String dir = "/home/eugene/dev/src/IdeaProjects/data";
	private Map<String, FileMeteringDataRawReader> mapReaders = new HashMap<>();

	@PostConstruct
	public void init() {
		mapReaders.put("hour/csv", 	new CsvHourMeteringDataRawReader(hourMeteringDataQueueService));
		mapReaders.put("hour/xml", 	new XmlHourMeteringDataRawReader(hourMeteringDataQueueService));
		mapReaders.put("day/csv", 	new CsvDayMeteringDataRawReader(dayMeteringDataQueueService));
		mapReaders.put("day/xml", 	new XmlDayMeteringDataRawReader(dayMeteringDataQueueService));
		mapReaders.put("month/csv", 	new CsvMonthMeteringDataRawReader(monthMeteringDataQueueService));
		mapReaders.put("month/xml", 	new XmlMonthMeteringDataRawReader(monthMeteringDataQueueService));
	}

	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		Arrays.asList("hour", "day", "month").stream().forEach(subDir -> {
			for (Path p : getListFiles(Paths.get(dir + "/" + subDir))) {
				try {
					String extension = FilenameUtils.getExtension(p.toString());
					FileMeteringDataRawReader reader = mapReaders.get(subDir + "/" + extension);
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


	@Inject
	private MeteringDataQueueService<HourMeteringDataRaw> hourMeteringDataQueueService;

	@Inject
	private MeteringDataQueueService<DayMeteringDataRaw> dayMeteringDataQueueService;

	@Inject
	private MeteringDataQueueService<MonthMeteringDataRaw> monthMeteringDataQueueService;
}
