package kz.kegoc.bln.service.doc.impl.helper;

import kz.kegoc.bln.entity.doc.DocMeteringReadingHeader;
import kz.kegoc.bln.entity.doc.translate.DocMeteringReadingHeaderTranslate;
import kz.kegoc.bln.entity.common.Lang;
import kz.kegoc.bln.service.common.EntityHelperService;
import kz.kegoc.bln.service.doc.DocMeteringReadingHeaderService;
import kz.kegoc.bln.service.doc.DocTypeService;
import kz.kegoc.bln.service.doc.GroupService;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.HashMap;

@Stateless
public class DocMeteringReadingHeaderHelperImpl implements EntityHelperService<DocMeteringReadingHeader> {
    public DocMeteringReadingHeader addDependencies(DocMeteringReadingHeader entity) {
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

        return addTranslation(entity);
    }


    public DocMeteringReadingHeader addTranslation(DocMeteringReadingHeader entity) {
        Lang lang = entity.getLang()!=null ? entity.getLang() : defLang;
        if (entity.getTranslations()==null)
            entity.setTranslations(new HashMap<>());

        DocMeteringReadingHeaderTranslate translate = entity.getTranslations().getOrDefault(lang, new DocMeteringReadingHeaderTranslate());
        translate.setLang(lang);
        translate.setHeader(entity);
        translate.setName(entity.getName());
        entity.getTranslations().put(lang, translate);

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
