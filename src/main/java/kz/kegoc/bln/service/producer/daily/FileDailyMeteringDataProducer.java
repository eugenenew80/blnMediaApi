package kz.kegoc.bln.service.producer.daily;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.inject.Inject;
import kz.kegoc.bln.entity.media.DailyMeteringData;
import kz.kegoc.bln.service.producer.common.MeteringDataProducer;
import kz.kegoc.bln.service.queue.DailyMeteringDataQueueService;

public abstract class FileDailyMeteringDataProducer implements MeteringDataProducer {
	private String dir = "C:\\\\src\\\\bln\\\\meteringData";
    private String subDir;
	
	public FileDailyMeteringDataProducer(String subDir) {
		this.subDir = subDir;
	}
	
    public FileDailyMeteringDataProducer(String dir, String subDir) {
		this.dir = dir;
		this.subDir = subDir;
	}


	public void execute() {
		for (Path p : getListFiles(Paths.get(dir + "\\" + subDir)))
			loadFile(p);
    }
    
	
	protected List<Path> getListFiles(Path path) {
		List<Path> list;
		try {
			list = Files.list(path).collect(Collectors.toList());
		} 
		catch (IOException e) {
			list = Collections.emptyList();
		}
		return list;
	}
	
	
	protected void loadFile(Path path) {
		try {
			List<DailyMeteringData> list = loadFromFile(path);
			service.addMeteringListData(list);
			Files.delete(path);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected abstract List<DailyMeteringData> loadFromFile(Path path) throws Exception;
	
	@Inject
	private DailyMeteringDataQueueService service;
}
