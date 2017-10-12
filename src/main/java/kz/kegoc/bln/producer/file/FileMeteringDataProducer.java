package kz.kegoc.bln.producer.file;

import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.entity.media.raw.DayMeteringDataRaw;
import kz.kegoc.bln.entity.media.raw.HourMeteringDataRaw;
import kz.kegoc.bln.entity.media.raw.MonthMeteringDataRaw;
import kz.kegoc.bln.producer.common.MeteringDataProducer;
import kz.kegoc.bln.producer.file.day.CsvDayMeteringDataReader;
import kz.kegoc.bln.producer.file.day.XmlDayMeteringDataReader;
import kz.kegoc.bln.producer.file.hour.CsvHourMeteringDataReader;
import kz.kegoc.bln.producer.file.hour.XmlHourMeteringDataReader;
import kz.kegoc.bln.producer.file.month.CsvMonthMeteringDataReader;
import kz.kegoc.bln.producer.file.month.XmlMonthMeteringDataReader;
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
public class FileMeteringDataProducer<T extends HasId> implements MeteringDataProducer {
	private String dir = "/home/eugene/dev/src/IdeaProjects/data";
	private Map<String, MeteringDataFileReader> mapReaders = new HashMap<>();

	@PostConstruct
	public void init() {
		mapReaders.put("hour/csv", 	new CsvHourMeteringDataReader(hourMeteringDataQueueService));
		mapReaders.put("hour/xml", 	new XmlHourMeteringDataReader(hourMeteringDataQueueService));
		mapReaders.put("day/csv", 	new CsvDayMeteringDataReader(dayMeteringDataQueueService));
		mapReaders.put("day/xml", 	new XmlDayMeteringDataReader(dayMeteringDataQueueService));
		mapReaders.put("month/csv", 	new CsvMonthMeteringDataReader(monthMeteringDataQueueService));
		mapReaders.put("month/xml", 	new XmlMonthMeteringDataReader(monthMeteringDataQueueService));
	}

	@Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
	public void execute() {
		Arrays.asList("hour", "day", "month").stream().forEach(subDir -> {
			for (Path p : getListFiles(Paths.get(dir + "/" + subDir))) {
				try {
					String extension = FilenameUtils.getExtension(p.toString());
					MeteringDataFileReader reader = mapReaders.get(subDir + "/" + extension);
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
