package kz.kegoc.bln.translator.impl;

import kz.kegoc.bln.entity.media.doc.DocMeteringReadingHeader;
import kz.kegoc.bln.entity.media.doc.translate.DocMeteringReadingHeaderTranslate;
import kz.kegoc.bln.entity.media.Lang;
import kz.kegoc.bln.entity.media.doc.translate.GroupTranslate;
import kz.kegoc.bln.translator.Translator;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class DocMeteringReadingHeaderTranslatorImpl implements Translator<DocMeteringReadingHeader> {
    public DocMeteringReadingHeader translate(DocMeteringReadingHeader entity, Lang lang) {
        entity.setLang(lang);

        if (entity.getTranslations()==null)
            return entity;

        DocMeteringReadingHeaderTranslate translate = entity.getTranslations().get(lang);
        if (translate==null)
            translate = entity.getTranslations().get(defLang);

        if (translate!=null)
            entity.setName(translate.getName());

        if (entity.getGroup()!=null && entity.getGroup().getTranslations()!=null) {
            GroupTranslate groupTranslate = entity.getGroup().getTranslations().get(lang);
            if (groupTranslate == null)
                groupTranslate = entity.getGroup().getTranslations().get(defLang);

            if (groupTranslate != null)
                entity.getGroup().setName(groupTranslate.getName());
        }

        return entity;
    }

    @Inject
    private Lang defLang;
}
