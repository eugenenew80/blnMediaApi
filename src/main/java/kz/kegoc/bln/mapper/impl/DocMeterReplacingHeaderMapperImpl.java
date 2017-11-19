package kz.kegoc.bln.mapper.impl;

import kz.kegoc.bln.entity.dict.Meter;
import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.entity.media.Lang;
import kz.kegoc.bln.entity.media.doc.DocMeterReplacingHeader;
import kz.kegoc.bln.entity.media.doc.DocType;
import kz.kegoc.bln.entity.media.doc.translate.DocMeterReplacingHeaderTranslate;
import kz.kegoc.bln.entity.media.doc.translate.DocMeteringReadingHeaderTranslate;
import kz.kegoc.bln.mapper.EntityMapper;
import kz.kegoc.bln.service.dict.MeterService;
import kz.kegoc.bln.service.dict.MeteringPointService;
import kz.kegoc.bln.service.media.doc.DocMeterReplacingHeaderService;
import kz.kegoc.bln.service.media.doc.DocTypeService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.HashMap;

@Stateless
public class DocMeterReplacingHeaderMapperImpl implements EntityMapper<DocMeterReplacingHeader> {
    public DocMeterReplacingHeader map(DocMeterReplacingHeader entity) {
        if (entity.getDocType()==null)
            entity.setDocType(docTypeService.findByCode("DocMeterReplacing"));

        if (entity.getMeteringPoint()!=null)
            entity.setMeteringPoint(meteringPointService.findById(entity.getMeteringPoint().getId()));

        if (entity.getOldMeter()!=null)
            entity.setOldMeter(meterService.findById(entity.getOldMeter().getId()));

        if (entity.getNewMeter()!=null)
            entity.setNewMeter(meterService.findById(entity.getNewMeter().getId()));

        if (entity.getId()!=null) {
            DocMeterReplacingHeader curEntity = headerService.findById(entity.getId());
            if (entity.getLines()==null)
                entity.setLines(curEntity.getLines());

            if (entity.getTranslations()==null)
                entity.setTranslations(curEntity.getTranslations());
        }

        return translate(entity);
    }


    private DocMeterReplacingHeader translate(DocMeterReplacingHeader entity) {
        Lang lang = entity.getLang();
        if (lang==null)
            lang = defLang;

        if (entity.getTranslations()==null)
            entity.setTranslations(new HashMap<>());

        DocMeterReplacingHeaderTranslate curTranslate = entity.getTranslations()
            .get(lang);

        if (curTranslate!=null)
            curTranslate.setName(entity.getName());
        else {
            DocMeterReplacingHeaderTranslate translate = new DocMeterReplacingHeaderTranslate();
            translate.setLang(lang);
            translate.setHeader(entity);
            translate.setName(entity.getName());
            entity.getTranslations().put(lang, translate);
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

    @Inject
    private Lang defLang;
}
