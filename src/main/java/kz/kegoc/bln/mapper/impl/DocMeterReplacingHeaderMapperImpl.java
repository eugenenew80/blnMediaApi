package kz.kegoc.bln.mapper.impl;

import kz.kegoc.bln.entity.dict.Meter;
import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.entity.media.doc.DocMeterReplacingHeader;
import kz.kegoc.bln.entity.media.doc.DocType;
import kz.kegoc.bln.mapper.EntityMapper;
import kz.kegoc.bln.service.dict.MeterService;
import kz.kegoc.bln.service.dict.MeteringPointService;
import kz.kegoc.bln.service.media.doc.DocMeterReplacingHeaderService;
import kz.kegoc.bln.service.media.doc.DocTypeService;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class DocMeterReplacingHeaderMapperImpl implements EntityMapper<DocMeterReplacingHeader> {
    public DocMeterReplacingHeader map(DocMeterReplacingHeader entity) {
        if (entity.getDocDate()==null)
            entity.setDocType(docTypeService.findByCode("DocMeterReplacing"));

        if (entity.getMeteringPoint()!=null)
            entity.setMeteringPoint(meteringPointService.findById(entity.getMeteringPoint().getId()));

        if (entity.getOldMeter()!=null)
            entity.setOldMeter(meterService.findById(entity.getOldMeter().getId()));

        if (entity.getNewMeter()!=null)
            entity.setNewMeter(meterService.findById(entity.getNewMeter().getId()));

        if (entity.getId()!=null) {
            DocMeterReplacingHeader header = headerService.findById(entity.getId());
            if (entity.getLines()==null)
                entity.setLines(header.getLines());
        }

        return entity;
    }


    @Inject
    private DocMeterReplacingHeaderService headerService;

    @Inject
    private DocTypeService docTypeService;

    @Inject
    private MeteringPointService meteringPointService;

    @Inject
    private MeterService meterService;
}
