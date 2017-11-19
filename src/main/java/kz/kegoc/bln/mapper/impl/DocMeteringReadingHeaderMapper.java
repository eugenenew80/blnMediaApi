package kz.kegoc.bln.mapper.impl;

import kz.kegoc.bln.entity.media.oper.DocMeteringReadingHeader;
import kz.kegoc.bln.entity.media.oper.translate.DocMeteringReadingHeaderTranslate;
import kz.kegoc.bln.entity.media.raw.Lang;
import kz.kegoc.bln.mapper.EntityMapper;
import kz.kegoc.bln.service.media.oper.DocMeteringReadingHeaderService;
import kz.kegoc.bln.service.media.oper.DocTypeService;
import kz.kegoc.bln.service.media.oper.GroupService;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;

@Stateless
public class DocMeteringReadingHeaderMapper implements EntityMapper<DocMeteringReadingHeader> {
    public DocMeteringReadingHeader map(DocMeteringReadingHeader entity) {

        if (entity.getDocType()==null)
            entity.setDocType(docTypeService.findByCode("DocMeteringReading"));

        if (entity.getId()!=null) {
            DocMeteringReadingHeader curEntity = headerService.findById(entity.getId());
            entity.setLines(curEntity.getLines());
            entity.setTranslations(curEntity.getTranslations());
        }

        if (entity.getTranslations()==null)
            entity.setTranslations(new HashMap<>());

        if (entity.getLines()==null)
            entity.setLines(new ArrayList<>());

        if (entity.getGroup()!=null)
            entity.setGroup(groupService.findById(entity.getGroup().getId()));

        addTranslation(entity);

        return entity;
    }


    private void addTranslation(DocMeteringReadingHeader entity) {
        Lang lang = entity.getLang();
        if (lang==null)
            lang = Lang.RU;

        DocMeteringReadingHeaderTranslate translate = new DocMeteringReadingHeaderTranslate();
        translate.setLang(lang);
        translate.setHeader(entity);
        translate.setName(entity.getName());

        DocMeteringReadingHeaderTranslate curTranslate = entity.getTranslations()
            .get(lang);

        if (curTranslate!=null)
            curTranslate.setName(translate.getName());
        else
            entity.getTranslations().put(lang, translate);
    }


    @Inject
    private DocTypeService docTypeService;

    @Inject
    private GroupService groupService;

    @Inject
    private DocMeteringReadingHeaderService headerService;
}
