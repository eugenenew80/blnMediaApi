package kz.kegoc.bln.service.media.oper.impl;

import com.google.common.collect.BiMap;
import kz.kegoc.bln.ejb.cdi.annotation.ParamCodes;
import kz.kegoc.bln.entity.dict.MeteringPointMeter;
import kz.kegoc.bln.entity.media.DataSource;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.oper.*;
import kz.kegoc.bln.entity.media.raw.DayMeteringBalanceRaw;
import kz.kegoc.bln.repository.media.oper.DocMeteringReadingLineRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.oper.DocMeteringReadingHeaderService;
import kz.kegoc.bln.service.media.oper.DocMeteringReadingLineService;
import kz.kegoc.bln.service.media.oper.GroupService;
import kz.kegoc.bln.service.media.raw.MeteringDataRawService;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Stateless
public class DocMeteringReadingLineServiceImpl
    extends AbstractEntityService<DocMeteringReadingLine>
        implements DocMeteringReadingLineService {

	@Inject
    public DocMeteringReadingLineServiceImpl(DocMeteringReadingLineRepository repository, Validator validator) {
        super(repository, validator);
        this.lineRepository = repository;
    }


    public List<DocMeteringReadingLine> findByHeader(Long headerId) {
        DocMeteringReadingHeader header = headerService.findById(headerId);
        if (header.getLines().size()==0)
            return saveLines(createLines(headerId));

        return header.getLines();
    }


    private List<DocMeteringReadingLine> createLines(Long headerId) {
        DocMeteringReadingHeader header = headerService.findById(headerId);
        Group group = header.getGroup();

        List<DocMeteringReadingLine> lines =
            group.getMeteringPoints().stream()
                .map(GroupMeteringPoint::getMeteringPoint)
                .map(point -> point.getMeters().stream()
                    .map(MeteringPointMeter::getMeter)
                    .map(meter -> paramCodes.keySet().stream()
                            .filter(paramCode -> paramCode.contains("A") && !paramCode.contains("B") )
                            .map(param -> {
                                DocMeteringReadingLine docLine = new DocMeteringReadingLine();
                                docLine.setMeteringPoint(point);
                                docLine.setMeter(meter);
                                docLine.setParamCode(param);
                                docLine.setHeader(header);
                                docLine.setDataSource(DataSource.NOT_SET);
                                docLine.setWayEntering(WayEntering.NOT_SET);
                                docLine.setUnitCode("кВт.ч");
                                return docLine;
                            })
                            .collect(Collectors.toList()))
                    .flatMap(p -> p.stream())
                    .collect(Collectors.toList()))
                .flatMap(l -> l.stream())
                .collect(Collectors.toList());

        return lines;
	}


	public List<DocMeteringReadingLine> autoFill(Long headerId) {
        DocMeteringReadingHeader header = headerService.findById(headerId);

        Map<String, String> params = new HashMap<>();
        params.put("A+", "AB+");
        params.put("A-", "AB-");
        params.put("R+", "RB+");
        params.put("R-", "RB-");


        List<DocMeteringReadingLine> lines = header.getLines().stream()
            .map(docLine -> {
                docLine.setStartBalance(0d);
                docLine.setEndBalance(0d);

                List<DayMeteringBalanceRaw> dayBalanceList = dayMeteringBalanceService.findReadyData(
                    docLine.getMeteringPoint().getId(),
                    header.getStartDate(),
                    params.get(docLine.getParamCode())
                );
                if (dayBalanceList != null && dayBalanceList.size() > 0) {
                    DayMeteringBalanceRaw dayBalance = dayBalanceList.get(0);
                    docLine.setStartBalance(dayBalance.getVal());
                    docLine.setDataSource(dayBalance.getDataSource());
                    docLine.setWayEntering(dayBalance.getWayEntering());
                }

                dayBalanceList = dayMeteringBalanceService.findReadyData(
                    docLine.getMeteringPoint().getId(),
                    header.getStartDate().plusDays(1),
                    params.get(docLine.getParamCode())
                );
                if (dayBalanceList != null && dayBalanceList.size() > 0) {
                    DayMeteringBalanceRaw dayBalance = dayBalanceList.get(0);
                    docLine.setEndBalance(dayBalance.getVal());
                    docLine.setDataSource(dayBalance.getDataSource());
                    docLine.setWayEntering(dayBalance.getWayEntering());
                }

                return docLine;
            })
            .collect(Collectors.toList());

        return saveLines(lines);
    }


    public DocMeteringReadingLine create(DocMeteringReadingLine entity) {
        prePersist(entity);
        return super.create(entity);
    }


    public DocMeteringReadingLine update(DocMeteringReadingLine entity) {
        prePersist(entity);
        return super.update(entity);
    }


    private void prePersist(DocMeteringReadingLine entity) {
        if (entity.getMeteringPoint().getMeteringPointType().getCode().equals("01")) {
            if (entity.getStartBalance() == null)
                entity.setStartBalance(0d);

            if (entity.getEndBalance() == null)
                entity.setEndBalance(0d);

            entity.setFlow(Math.round((entity.getEndBalance() - entity.getStartBalance()) * 100d) / 100d);
        }

        if (entity.getMeteringPoint().getMeteringPointType().getCode().equals("02")) {
            entity.setStartBalance(null);
            entity.setEndBalance(null);
        }
    }


    private List<DocMeteringReadingLine> saveLines(List<DocMeteringReadingLine> lines) {
        List<DocMeteringReadingLine> savedLines = new ArrayList<>();
        for (DocMeteringReadingLine line : lines) {
            DocMeteringReadingLine savedLine;
            if (line.getId()==null)
                savedLine = create(line);
            else
                savedLine = update(line);
            savedLines.add(savedLine);
        }

        return savedLines;
    }


    private DocMeteringReadingLineRepository lineRepository;

    @Inject
    private DocMeteringReadingHeaderService headerService;

    @Inject
    private MeteringDataRawService<DayMeteringBalanceRaw> dayMeteringBalanceService;

    @Inject @ParamCodes
    private BiMap<String, String> paramCodes;
}
