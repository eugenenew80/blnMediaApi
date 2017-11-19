package kz.kegoc.bln.mapper.impl;

import kz.kegoc.bln.entity.media.doc.DocMeteringReadingHeader;
import kz.kegoc.bln.entity.media.doc.translate.DocMeteringReadingHeaderTranslate;
import kz.kegoc.bln.entity.media.Lang;
import kz.kegoc.bln.mapper.EntityMapper;
import kz.kegoc.bln.service.media.doc.DocMeteringReadingHeaderService;
import kz.kegoc.bln.service.media.doc.DocTypeService;
import kz.kegoc.bln.service.media.doc.GroupService;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.HashMap;

@Stateless
public class DocMeteringReadingHeaderMapperImpl implements EntityMapper<DocMeteringReadingHeader> {
    public DocMeteringReadingHeader map(DocMeteringReadingHeader entity) {
        if (entity.getDocType()==null)
            entity.setDocType(docTypeService.findByCode("DocMeteringReading"));

        if (entity.getGroup()!=null)
            entity.setGroup(groupService.findById(entity.getGroup().getId()));

        if (entity.getId()!=null) {
            DocMeteringReadingHeader curEntity = headerService.findById(entity.getId());
            if (entity.getLines()==null)
                entity.setLines(curEntity.getLines());

            if (entity.getTranslations()==null)
                entity.setTranslations(curEntity.getTranslations());
        }

        return translate(entity);
    }


    private DocMeteringReadingHeader translate(DocMeteringReadingHeader entity) {
        Lang lang = entity.getLang();
        if (lang==null)
            lang = defLang;

        if (entity.getTranslations()==null)
            entity.setTranslations(new HashMap<>());

        DocMeteringReadingHeaderTranslate curTranslate = entity.getTranslations()
            .get(lang);

        if (curTranslate!=null)
            curTranslate.setName(entity.getName());
        else {
            DocMeteringReadingHeaderTranslate translate = new DocMeteringReadingHeaderTranslate();
            translate.setLang(lang);
            translate.setHeader(entity);
            translate.setName(entity.getName());
            entity.getTranslations().put(lang, translate);
        }

        return entity;
    }


    @Inject
    private DocTypeService docTypeService;

    @Inject
    private GroupService groupService;

    @Inject
    private DocMeteringReadingHeaderService headerService;

    @Inject
    private Lang defLang;
}
