package kz.kegoc.bln.mapper.impl;

import kz.kegoc.bln.entity.media.Lang;
import kz.kegoc.bln.entity.media.doc.DocUnderAccountingHeader;
import kz.kegoc.bln.entity.media.doc.translate.DocUnderAccountingHeaderTranslate;
import kz.kegoc.bln.mapper.EntityMapper;
import kz.kegoc.bln.service.dict.MeterService;
import kz.kegoc.bln.service.dict.MeteringPointService;
import kz.kegoc.bln.service.media.doc.DocUnderAccountingHeaderService;
import kz.kegoc.bln.service.media.doc.DocTypeService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.HashMap;

@Stateless
public class DocUnderAccountingHeaderMapperImpl implements EntityMapper<DocUnderAccountingHeader> {
    public DocUnderAccountingHeader map(DocUnderAccountingHeader entity) {
        if (entity.getDocType()==null)
            entity.setDocType(docTypeService.findByCode("DocUnderAccounting"));

        if (entity.getMeteringPoint()!=null)
            entity.setMeteringPoint(meteringPointService.findById(entity.getMeteringPoint().getId()));

        if (entity.getMeter()!=null)
            entity.setMeter(meterService.findById(entity.getMeter().getId()));


        if (entity.getId()!=null) {
            DocUnderAccountingHeader curEntity = headerService.findById(entity.getId());
            if (entity.getMeasLines()==null)
                entity.setMeasLines(curEntity.getMeasLines());

            if (entity.getCalcLines()==null)
                entity.setCalcLines(curEntity.getCalcLines());

            if (entity.getTranslations()==null)
                entity.setTranslations(curEntity.getTranslations());
        }

        return translate(entity);
    }


    private DocUnderAccountingHeader translate(DocUnderAccountingHeader entity) {
        Lang lang = entity.getLang();
        if (lang==null)
            lang = defLang;

        if (entity.getTranslations()==null)
            entity.setTranslations(new HashMap<>());

        DocUnderAccountingHeaderTranslate curTranslate = entity.getTranslations()
            .get(lang);

        if (curTranslate!=null)
            curTranslate.setName(entity.getName());
        else {
            DocUnderAccountingHeaderTranslate translate = new DocUnderAccountingHeaderTranslate();
            translate.setLang(lang);
            translate.setHeader(entity);
            translate.setName(entity.getName());
            entity.getTranslations().put(lang, translate);
        }

        return entity;
    }


    @Inject
    private DocUnderAccountingHeaderService headerService;

    @Inject
    private DocTypeService docTypeService;

    @Inject
    private MeteringPointService meteringPointService;

    @Inject
    private MeterService meterService;

    @Inject
    private Lang defLang;
}
