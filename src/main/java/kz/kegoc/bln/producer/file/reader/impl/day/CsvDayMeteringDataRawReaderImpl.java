package kz.kegoc.bln.producer.file.reader.impl.day;

import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.day.DayMeteringDataRaw;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Stateless
@CSV
public class CsvDayMeteringDataRawReaderImpl implements FileMeteringReader<DayMeteringDataRaw> {
    private static final Logger logger = LoggerFactory.getLogger(CsvDayMeteringDataRawReaderImpl.class);

    @Inject
    public CsvDayMeteringDataRawReaderImpl(MeteringDataQueue<DayMeteringDataRaw> service) {
        this.queue =service;
    }

    public void read(Path path) {
        logger.info("CsvDayMeteringDataRawReaderImpl.read started");
        logger.info("file: " + path.toString());

        List<String> strs = readFile(path);

        logger.info("Parsing file content started");
        List<DayMeteringDataRaw> list = IntStream.range(1, strs.size())
            .mapToObj(i -> strs.get(i))
            .map(this::convert)
            .collect(toList());
        logger.info("Parsing file content completed, count of rows: " + list.size());

        logger.debug("Adding data to queue");
        queue.addAll(list);
        logger.info("CsvDayMeteringDataRawReaderImpl.read completed");
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

    private DayMeteringDataRaw convert(String s)  {
        String[] data = s.split(";");

        DayMeteringDataRaw d = new DayMeteringDataRaw();
        d.setMeteringDate(LocalDate.parse(data[0], DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        d.setExternalCode(data[1]);
        d.setParamCode(data[2]);
        d.setUnitCode(data[3]);
        d.setVal(Double.parseDouble(data[4]) );
        d.setWayEntering(WayEntering.CSV);
        d.setStatus(DataStatus.RAW);
        d.setDataSourceCode("MANUAL");
        return d;
    }

    private MeteringDataQueue<DayMeteringDataRaw> queue;
}
