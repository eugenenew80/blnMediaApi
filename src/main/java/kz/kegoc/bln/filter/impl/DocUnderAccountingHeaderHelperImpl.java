package kz.kegoc.bln.filter.impl;

import kz.kegoc.bln.entity.common.Lang;
import kz.kegoc.bln.entity.doc.DocUnderAccountingHeader;
import kz.kegoc.bln.entity.doc.translate.DocUnderAccountingHeaderTranslate;
import kz.kegoc.bln.filter.Filter;
import kz.kegoc.bln.service.dict.MeterService;
import kz.kegoc.bln.service.dict.MeteringPointService;
import kz.kegoc.bln.service.doc.DocUnderAccountingHeaderService;
import kz.kegoc.bln.service.doc.DocTypeService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.HashMap;

@Stateless
public class DocUnderAccountingHeaderHelperImpl implements Filter<DocUnderAccountingHeader> {
    public DocUnderAccountingHeader filter(DocUnderAccountingHeader entity) {
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
        Lang lang = entity.getLang()!=null ? entity.getLang() : defLang;
        if (entity.getTranslations()==null)
            entity.setTranslations(new HashMap<>());

        DocUnderAccountingHeaderTranslate translate = entity.getTranslations().getOrDefault(lang, new DocUnderAccountingHeaderTranslate());
        translate.setLang(lang);
        translate.setHeader(entity);
        translate.setName(entity.getName());
        entity.getTranslations().put(lang, translate);

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
