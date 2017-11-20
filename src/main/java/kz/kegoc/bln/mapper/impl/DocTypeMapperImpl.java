package kz.kegoc.bln.mapper.impl;

import kz.kegoc.bln.entity.media.Lang;
import kz.kegoc.bln.entity.media.doc.DocType;
import kz.kegoc.bln.entity.media.doc.translate.DocTypeTranslate;
import kz.kegoc.bln.mapper.EntityMapper;
import kz.kegoc.bln.service.media.doc.DocTypeService;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.HashMap;

@Stateless
public class DocTypeMapperImpl implements EntityMapper<DocType> {
    public DocType map(DocType entity) {
        if (entity.getId()!=null) {
            DocType curEntity = docTypeService.findById(entity.getId());

            if (entity.getTranslations()==null)
                entity.setTranslations(curEntity.getTranslations());
        }
        return translate(entity);
    }

    private DocType translate(DocType entity) {
        Lang lang = entity.getLang();
        if (lang==null)
            lang = defLang;

        if (entity.getTranslations()==null)
            entity.setTranslations(new HashMap<>());

        DocTypeTranslate curTranslate = entity.getTranslations()
            .get(lang);

        if (curTranslate!=null)
            curTranslate.setName(entity.getName());
        else {
            DocTypeTranslate translate = new DocTypeTranslate();
            translate.setLang(lang);
            translate.setDocType(entity);
            translate.setName(entity.getName());
            entity.getTranslations().put(lang, translate);
        }

        return entity;
    }


    @Inject
    private DocTypeService docTypeService;

    @Inject
    private Lang defLang;
}
