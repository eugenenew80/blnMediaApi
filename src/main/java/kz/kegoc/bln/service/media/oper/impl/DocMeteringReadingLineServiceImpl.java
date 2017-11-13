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
                                .filter(param -> param.contains("AB") )
                                .map(param -> mapToPoint(header, point, meter, param))
                                .collect(Collectors.toList()))
                        .flatMap(p -> p.stream())
                        .collect(Collectors.toList()))
                .flatMap(l -> l.stream())
                .collect(Collectors.toList());

        return lines;
	}


    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<DocMeteringReadingLine> autoFill(Long headerId) {
        DocMeteringReadingHeader header = headerService.findById(headerId);

        return
            header.getLines().stream()
                .map(d -> {
                    DayMeteringBalanceRaw dayBalance = new DayMeteringBalanceRaw();
                    dayBalance.setMeteringDate(header.getStartDate().atStartOfDay());
                    dayBalance.setExternalCode(d.getMeteringPoint().getExternalCode());
                    dayBalance.setParamCode(d.getParamCode());
                    dayBalance.setDataSource(d.getDataSource());
                    dayBalance.setWayEntering(d.getWayEntering());
                    dayBalance.setUnitCode(d.getUnitCode());
                    dayBalance.setStatus(DataStatus.RAW);

                    DayMeteringBalanceRaw dayBalanceStart = dayMeteringBalanceService.findByEntity(dayBalance);
                    d.setStartBalance(0d);
                    if (dayBalanceStart!=null)
                        d.setStartBalance(dayBalanceStart.getVal());

                    dayBalance.setMeteringDate(header.getStartDate().plusDays(1).atStartOfDay());
                    DayMeteringBalanceRaw dayBalanceEnd = dayMeteringBalanceService.findByEntity(dayBalance);
                    d.setEndBalance(0d);
                    if (dayBalanceEnd!=null)
                        d.setEndBalance(dayBalanceEnd.getVal());

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
