package kz.kegoc.bln.translator.impl;

import kz.kegoc.bln.entity.doc.DocMeteringReadingHeader;
import kz.kegoc.bln.entity.doc.Group;
import kz.kegoc.bln.entity.doc.translate.DocMeteringReadingHeaderTranslate;
import kz.kegoc.bln.entity.common.Lang;
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

        groupTranslator.translate(entity.getGroup(), lang);
        return entity;
    }

    @Inject
    private Lang defLang;

    @Inject
    private Translator<Group> groupTranslator;
}
