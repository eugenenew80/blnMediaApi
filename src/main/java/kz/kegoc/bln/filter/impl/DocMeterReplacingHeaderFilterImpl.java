package kz.kegoc.bln.filter.impl;

import kz.kegoc.bln.entity.common.Lang;
import kz.kegoc.bln.entity.doc.DocMeterReplacingHeader;
import kz.kegoc.bln.entity.doc.translate.DocMeterReplacingHeaderTranslate;
import kz.kegoc.bln.filter.Filter;
import kz.kegoc.bln.service.dict.MeterService;
import kz.kegoc.bln.service.dict.MeteringPointService;
import kz.kegoc.bln.service.doc.DocMeterReplacingHeaderService;
import kz.kegoc.bln.service.doc.DocTypeService;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.HashMap;

@Stateless
public class DocMeterReplacingHeaderFilterImpl implements Filter<DocMeterReplacingHeader> {
    public DocMeterReplacingHeader filter(DocMeterReplacingHeader entity) {
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
        Lang lang = entity.getLang()!=null ? entity.getLang() : defLang;
        if (entity.getTranslations()==null)
            entity.setTranslations(new HashMap<>());

        DocMeterReplacingHeaderTranslate translate = entity.getTranslations().getOrDefault(lang, new DocMeterReplacingHeaderTranslate());
        translate.setLang(lang);
        translate.setHeader(entity);
        translate.setName(entity.getName());
        entity.getTranslations().put(lang, translate);

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
