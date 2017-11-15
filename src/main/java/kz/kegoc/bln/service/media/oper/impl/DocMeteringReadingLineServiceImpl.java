package kz.kegoc.bln.service.media.oper.impl;

import com.google.common.collect.BiMap;
import kz.kegoc.bln.ejb.cdi.annotation.ParamCodes;
import kz.kegoc.bln.entity.dict.Meter;
import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.entity.dict.MeteringPointMeter;
import kz.kegoc.bln.entity.media.DataSource;
import kz.kegoc.bln.entity.media.DataStatus;
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
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.validation.Validator;
import java.util.List;
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
        List<DocMeteringReadingLine> lines = header.getLines();

        List<DocMeteringReadingLine> savedLines = lines;
        if (lines.size()==0) {
            for (DocMeteringReadingLine line : createLines(headerId)) {
                DocMeteringReadingLine savedLine = super.create(line);
                savedLines.add(savedLine);
            }
        }

        /*
        List<MeteringPoint> curMeteringPoints = savedLines.stream()
                .map(DocMeteringReadingLine::getMeteringPoint)
                .distinct()
                .collect(Collectors.toList());

        List<DocMeteringReadingLine> newLines = createLines(headerId)
                .stream()
                .filter(t -> !curMeteringPoints.contains(t.getMeteringPoint()))
                .collect(Collectors.toList());

        if (newLines.size()!=0) {
            for (DocMeteringReadingLine newLine : newLines) {
                DocMeteringReadingLine savedNewLine = super.create(newLine);
                savedLines.add(savedNewLine);
            }
        }
        */

        return savedLines;
    }




    public List<DocMeteringReadingLine> createLines(Long headerId) {
        DocMeteringReadingHeader header = headerService.findById(headerId);
        Group group = header.getTemplate().getGroup();

        List<DocMeteringReadingLine> lines =
            group.getMeteringPoints().stream()
                .map(GroupMeteringPoint::getMeteringPoint)
                .map(point -> point.getMeters().stream()
                        .map(MeteringPointMeter::getMeter)
                        .map(meter -> paramCodes.keySet().stream()
                                .filter(paramCode -> paramCode.contains("AB") )
                                .map(param -> {
                                    DocMeteringReadingLine docLine = new DocMeteringReadingLine();
                                    docLine.setMeteringPoint(point);
                                    docLine.setMeter(meter);
                                    docLine.setOperDate(header.getStartDate());
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

        List<DocMeteringReadingLine> lines = header.getLines().stream()
            .map(docLine -> {
                docLine.setStartBalance(0d);
                docLine.setEndBalance(0d);

                List<DayMeteringBalanceRaw> dayBalanceList = dayMeteringBalanceService.findReadyData(
                        docLine.getMeteringPoint().getId(),
                        header.getStartDate().atStartOfDay(),
                        docLine.getParamCode()
                );
                if (dayBalanceList != null && dayBalanceList.size() > 0) {
                    DayMeteringBalanceRaw dayBalance = dayBalanceList.get(0);
                    docLine.setStartBalance(dayBalance.getVal());
                    docLine.setDataSource(dayBalance.getDataSource());
                    docLine.setWayEntering(dayBalance.getWayEntering());
                }

                dayBalanceList = dayMeteringBalanceService.findReadyData(
                        docLine.getMeteringPoint().getId(),
                        header.getStartDate().plusDays(1).atStartOfDay(),
                        docLine.getParamCode()
                );
                if (dayBalanceList != null && dayBalanceList.size() > 0) {
                    DayMeteringBalanceRaw dayBalance = dayBalanceList.get(0);
                    docLine.setEndBalance(dayBalance.getVal());
                    docLine.setDataSource(dayBalance.getDataSource());
                    docLine.setWayEntering(dayBalance.getWayEntering());
                }

                docLine.setFlow(Math.round((docLine.getEndBalance() - docLine.getStartBalance()) * 100d) / 100d);
                return docLine;
            })
            .collect(Collectors.toList());

        return lines;
    }



    private DocMeteringReadingLineRepository lineRepository;

    @Inject
    private DocMeteringReadingHeaderService headerService;

    @Inject
    private GroupService groupService;

    @Inject
    private MeteringDataRawService<DayMeteringBalanceRaw> dayMeteringBalanceService;

    @Inject @ParamCodes
    private BiMap<String, String> paramCodes;
}
