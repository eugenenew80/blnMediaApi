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
        DocType docType = docTypeService.findByCode("DocMeterReplacing");
        entity.setDocType(docType);

        if (entity.getMeteringPoint()==null || entity.getMeteringPoint().getId()==null)
            entity.setMeteringPoint(null);
        else {
            MeteringPoint meteringPoint = meteringPointService.findById(entity.getMeteringPoint().getId());
            entity.setMeteringPoint(meteringPoint);
        }

        if (entity.getOldMeter()==null || entity.getOldMeter().getId()==null)
            entity.setOldMeter(null);
        else {
            Meter oldMeter = meterService.findById(entity.getOldMeter().getId());
            entity.setOldMeter(oldMeter);
        }

        if (entity.getNewMeter()==null || entity.getNewMeter().getId()==null)
            entity.setNewMeter(null);
        else {
            Meter newMeter = meterService.findById(entity.getNewMeter().getId());
            entity.setNewMeter(newMeter);
        }

        if (entity.getLines()==null && entity.getId()!=null) {
            DocMeterReplacingHeader header = headerService.findById(entity.getId());
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
