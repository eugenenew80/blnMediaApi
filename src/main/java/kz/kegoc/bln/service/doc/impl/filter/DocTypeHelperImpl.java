package kz.kegoc.bln.service.doc.impl.filter;

import kz.kegoc.bln.entity.common.Lang;
import kz.kegoc.bln.entity.doc.DocType;
import kz.kegoc.bln.entity.doc.translate.DocTypeTranslate;
import kz.kegoc.bln.service.common.Filter;
import kz.kegoc.bln.service.doc.DocTypeService;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.HashMap;

@Stateless
public class DocTypeHelperImpl implements Filter<DocType> {
    public DocType filter(DocType entity) {
        if (entity.getId()!=null) {
            DocType curEntity = docTypeService.findById(entity.getId());

            if (entity.getTranslations()==null)
                entity.setTranslations(curEntity.getTranslations());
        }
        return translate(entity);
    }

    private DocType translate(DocType entity) {
        Lang lang = entity.getLang()!=null ? entity.getLang() : defLang;
        if (entity.getTranslations()==null)
            entity.setTranslations(new HashMap<>());

        DocTypeTranslate translate = entity.getTranslations().getOrDefault(lang, new DocTypeTranslate());
        translate.setLang(lang);
        translate.setDocType(entity);
        translate.setName(entity.getName());
        entity.getTranslations().put(lang, translate);

        return entity;
    }

    @Inject
    private DocTypeService docTypeService;

    @Inject
    private Lang defLang;
}
