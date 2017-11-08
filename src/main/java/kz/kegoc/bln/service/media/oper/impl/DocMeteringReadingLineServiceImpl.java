package kz.kegoc.bln.service.media.oper.impl;

import com.google.common.collect.BiMap;
import kz.kegoc.bln.ejb.cdi.annotation.ParamCodes;
import kz.kegoc.bln.entity.dict.Meter;
import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.entity.dict.MeteringPointMeter;
import kz.kegoc.bln.entity.media.DataSource;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.oper.DocMeteringReadingHeader;
import kz.kegoc.bln.entity.media.oper.DocMeteringReadingLine;
import kz.kegoc.bln.entity.media.oper.Group;
import kz.kegoc.bln.entity.media.oper.GroupMeteringPoint;
import kz.kegoc.bln.repository.media.oper.DocMeteringReadingLineRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.oper.DocMeteringReadingHeaderService;
import kz.kegoc.bln.service.media.oper.DocMeteringReadingLineService;
import kz.kegoc.bln.service.media.oper.GroupService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class DocMeteringReadingLineServiceImpl
        extends AbstractEntityService<DocMeteringReadingLine>
                implements DocMeteringReadingLineService {

	@Inject
    public DocMeteringReadingLineServiceImpl(DocMeteringReadingLineRepository repository, Validator validator) {
        super(repository, validator);
    }


    public List<DocMeteringReadingLine> createLines(Long headerId, LocalDateTime operDate) {
        DocMeteringReadingHeader header = headerService.findById(headerId);
        Group group = header.getTemplate().getGroup();

        return
                group.getMeteringPoints().stream()
                        .map(GroupMeteringPoint::getMeteringPoint)
                        .map(point -> point.getMeters().stream()
                                .map(MeteringPointMeter::getMeter)
                                .map(meter -> paramCodes.keySet().stream()
                                        .filter(param -> param.contains("AB") )
                                        .map(param -> mapToPoint(header, point, meter, param, operDate))
                                        .collect(Collectors.toList()))
                                .flatMap(p -> p.stream())
                                .collect(Collectors.toList()))
                        .flatMap(l -> l.stream())
                        .collect(Collectors.toList());
    }


    private DocMeteringReadingLine mapToPoint(DocMeteringReadingHeader header, MeteringPoint point, Meter meter, String param, LocalDateTime operDate) {
        DocMeteringReadingLine d = new DocMeteringReadingLine();
        d.setMeteringPoint(point);
        d.setMeter(meter);
        d.setOperDate(operDate.toLocalDate());
        d.setParamCode(param);
        d.setUnitCode("kWh");
        d.setWayEntering(WayEntering.AUTO);
        d.setDataSource(DataSource.EMCOS);
        d.setHeader(header);
        return d;
    }


    @Inject
    private DocMeteringReadingHeaderService headerService;

    @Inject
    private GroupService groupService;

    @Inject @ParamCodes
    private BiMap<String, String> paramCodes;
}
