package kz.kegoc.bln.producer.file.day;

import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.raw.DayMeteringDataRaw;
import kz.kegoc.bln.producer.file.FileMeteringDataRawReader;
import kz.kegoc.bln.queue.MeteringDataQueueService;
import kz.kegoc.bln.annotation.CSV;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Stateless
@CSV
public class CsvDayMeteringDataRawReader implements FileMeteringDataRawReader<DayMeteringDataRaw> {

    @Inject
    public CsvDayMeteringDataRawReader(MeteringDataQueueService<DayMeteringDataRaw> service) {
        this.service=service;
    }

    public void loadFromFile(Path path) throws Exception {
        List<DayMeteringDataRaw> list = new ArrayList<>();
        List<String> strs = Files.readAllLines(path);
        for (int i=1; i<strs.size(); i++ ) {
            list.add(convert(strs.get(i)));
        }

        service.addAll(list);
    }

    private DayMeteringDataRaw convert(String s) throws Exception {
        String[] data = s.split(";");

        DayMeteringDataRaw d = new DayMeteringDataRaw();
        d.setMeteringDate(LocalDate.parse(data[0], DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        d.setExternalCode(data[1]);
        d.setParamCode(data[2]);
        d.setUnitCode(data[3]);
        d.setVal( Double.parseDouble(data[4]) );
        d.setWayEntering(WayEntering.CSV);
        d.setStatus(DataStatus.RAW);
        d.setDataSourceCode("MANUAL");

        return d;
    }

    private MeteringDataQueueService<DayMeteringDataRaw> service;
}
