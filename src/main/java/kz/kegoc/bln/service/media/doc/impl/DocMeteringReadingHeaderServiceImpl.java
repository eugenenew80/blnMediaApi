package kz.kegoc.bln.service.media.doc.impl;

import com.google.common.collect.BiMap;
import kz.kegoc.bln.ejb.cdi.annotation.ParamCodes;
import kz.kegoc.bln.entity.dict.MeteringPointMeter;
import kz.kegoc.bln.entity.media.DataSource;
import kz.kegoc.bln.entity.media.doc.DocMeteringReadingHeader;
import kz.kegoc.bln.entity.media.doc.DocMeteringReadingLine;
import kz.kegoc.bln.entity.media.doc.Group;
import kz.kegoc.bln.entity.media.doc.GroupMeteringPoint;
import kz.kegoc.bln.entity.media.data.DayMeteringBalance;
import kz.kegoc.bln.repository.media.doc.DocMeteringReadingHeaderRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.doc.DocMeteringReadingHeaderService;
import kz.kegoc.bln.service.media.doc.DocMeteringReadingLineService;
import kz.kegoc.bln.service.media.data.MeteringDataService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Stateless
public class DocMeteringReadingHeaderServiceImpl
    extends AbstractEntityService<DocMeteringReadingHeader>
        implements DocMeteringReadingHeaderService {

	@Inject
    public DocMeteringReadingHeaderServiceImpl(DocMeteringReadingHeaderRepository repository, Validator validator) {
        super(repository, validator);
    }


    public DocMeteringReadingHeader create(DocMeteringReadingHeader entity) {
        entity.setLines(createLines(entity));
        return super.create(entity);
    }


    public List<DocMeteringReadingLine> createLines(DocMeteringReadingHeader header) {
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
        DocMeteringReadingHeader header = findById(headerId);

        Map<String, String> params = new HashMap<>();
        params.put("A+", "AB+");
        params.put("A-", "AB-");
        params.put("R+", "RB+");
        params.put("R-", "RB-");


        List<DocMeteringReadingLine> lines = header.getLines().stream()
            .map(docLine -> {
                docLine.setStartBalance(0d);
                docLine.setEndBalance(0d);
                docLine.setDataSource(DataSource.NOT_SET);

                List<DayMeteringBalance> dayBalanceList = dayMeteringBalanceService.findReadyData(
                    docLine.getMeteringPoint().getId(),
                    header.getStartDate(),
                    params.get(docLine.getParamCode())
                );
                if (dayBalanceList != null && dayBalanceList.size() > 0) {
                    DayMeteringBalance dayBalance = dayBalanceList.get(0);
                    docLine.setStartBalance(dayBalance.getVal());
                    docLine.setDataSource(DataSource.EMCOS);
                }

                dayBalanceList = dayMeteringBalanceService.findReadyData(
                    docLine.getMeteringPoint().getId(),
                    header.getEndDate(),
                    params.get(docLine.getParamCode())
                );
                if (dayBalanceList != null && dayBalanceList.size() > 0) {
                    DayMeteringBalance dayBalance = dayBalanceList.get(0);
                    docLine.setEndBalance(dayBalance.getVal());
                    docLine.setDataSource(DataSource.EMCOS);
                }

                return docLine;
            })
            .collect(Collectors.toList());

        return lines;
    }


    @Inject
    private MeteringDataService<DayMeteringBalance> dayMeteringBalanceService;

    @Inject @ParamCodes
    private BiMap<String, String> paramCodes;
}
