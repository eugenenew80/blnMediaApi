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
import javax.inject.Inject;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.ArrayList;
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


    public List<DocMeteringReadingLine> findByGroup(Long headerId, Long groupId, LocalDateTime operDate) {
        List<MeteringPoint> meteringPointsInGroup = groupService.findById(groupId).getMeteringPoints()
                .stream()
                .map(m -> m.getMeteringPoint())
                .collect(Collectors.toList());

        List<DocMeteringReadingLine> operData = lineRepository.findByHeader(headerId)
                .stream()
                .filter(m -> meteringPointsInGroup.contains(m.getMeteringPoint()))
                .collect(Collectors.toList());

        List<MeteringPoint> curMeteringPoints = operData.stream()
                .map(DocMeteringReadingLine::getMeteringPoint)
                .distinct()
                .collect(Collectors.toList());

        List<DocMeteringReadingLine> newOperData = createLines(headerId)
                .stream()
                .filter(t -> !curMeteringPoints.contains(t.getMeteringPoint()))
                .collect(Collectors.toList());

        operData.addAll(newOperData);
        return operData;
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
                                    .filter(param -> param.contains("AB") )
                                    .map(param -> mapToPoint(header, point, meter, param))
                                    .collect(Collectors.toList()))
                            .flatMap(p -> p.stream())
                            .collect(Collectors.toList()))
                    .flatMap(l -> l.stream())
                    .collect(Collectors.toList());

        List<DocMeteringReadingLine> savedLines = new ArrayList<>();
        lines.forEach(line -> {
            DocMeteringReadingLine savedLine = super.create(line);
            savedLines.add(savedLine);
        });

        return savedLines;
	}


    public List<DocMeteringReadingLine> autoFill(Long headerId) {
        DocMeteringReadingHeader header = headerService.findById(headerId);
        Group group = header.getTemplate().getGroup();

	    return
                findByGroup(header.getId(), group.getId(), header.getStartDate().atStartOfDay()).stream()
                        .map(d -> {
                            DayMeteringBalanceRaw b = new DayMeteringBalanceRaw();
                            b.setMeteringDate(d.getOperDate().minusDays(1).atStartOfDay());
                            b.setExternalCode(d.getMeteringPoint().getExternalCode());
                            b.setParamCode(d.getParamCode());
                            b.setDataSource(d.getDataSource());
                            b.setWayEntering(d.getWayEntering());
                            b.setUnitCode(d.getUnitCode());
                            b.setStatus(DataStatus.RAW);

                            DayMeteringBalanceRaw balanceStart = dayMeteringBalanceService.findByEntity(b);
                            d.setStartBalance(0d);
                            if (balanceStart!=null)
                                d.setStartBalance(balanceStart.getVal());

                            b.setMeteringDate(header.getStartDate().atStartOfDay());
                            DayMeteringBalanceRaw balanceEnd = dayMeteringBalanceService.findByEntity(b);
                            d.setEndBalance(0d);
                            if (balanceEnd!=null)
                                d.setEndBalance(balanceEnd.getVal());

                            d.setFlow(Math.round((d.getEndBalance() - d.getStartBalance())*100d ) / 100d);
                            return d;
                        })
                        .collect(Collectors.toList());
    }


    private DocMeteringReadingLine mapToPoint(DocMeteringReadingHeader header, MeteringPoint point, Meter meter, String param) {
        DocMeteringReadingLine d = new DocMeteringReadingLine();
        d.setMeteringPoint(point);
        d.setMeter(meter);
        d.setOperDate(header.getStartDate());
        d.setParamCode(param);
        d.setUnitCode("kWh");
        d.setWayEntering(WayEntering.AUTO);
        d.setDataSource(DataSource.EMCOS);
        d.setHeader(header);
        return d;
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
