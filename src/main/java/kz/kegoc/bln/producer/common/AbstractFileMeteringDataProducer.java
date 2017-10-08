package kz.kegoc.bln.producer.common;

import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.queue.common.MeteringDataQueueService;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractFileMeteringDataProducer<T extends HasId> implements MeteringDataProducer {
	private String dir = "/home/eugene/dev/src/IdeaProjects/data";
    private String subDir;

	public AbstractFileMeteringDataProducer() {
	}

    public AbstractFileMeteringDataProducer(String subDir) {
		this.subDir = subDir;
	}

    public AbstractFileMeteringDataProducer(String dir, String subDir) {
		this.dir = dir;
		this.subDir = subDir;
	}


	public void execute() {
		for (Path p : getListFiles(Paths.get(dir + "/" + subDir)))
			loadFile(p);
    }
    
	
	protected List<Path> getListFiles(Path path) {
		List<Path> list;
		try (Stream<Path> stream = Files.list(path))  {
			list = stream.collect(Collectors.toList());
		} 
		catch (IOException e) {
			list = Collections.emptyList();
		}
		return list;
	}
	
	
	protected void loadFile(Path path) {
		try {
			List<T> list = loadFromFile(path);
			service.addMeteringListData(list);
			Files.delete(path);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected abstract List<T> loadFromFile(Path path) throws Exception;
	
	@Inject
	private MeteringDataQueueService<T> service;
}
