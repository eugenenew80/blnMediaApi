package kz.kegoc.bln.translator.impl;

import kz.kegoc.bln.entity.media.Lang;
import kz.kegoc.bln.entity.media.doc.DocUnderAccountingHeader;
import kz.kegoc.bln.entity.media.doc.translate.DocUnderAccountingHeaderTranslate;
import kz.kegoc.bln.translator.Translator;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class DocUnderAccountingHeaderTranslatorImpl implements Translator<DocUnderAccountingHeader> {
    public DocUnderAccountingHeader translate(DocUnderAccountingHeader entity, Lang lang) {
        entity.setLang(lang);

        if (entity.getTranslations()==null)
            return entity;

        DocUnderAccountingHeaderTranslate translate = entity.getTranslations().get(lang);
        if (translate==null)
            translate = entity.getTranslations().get(defLang);

        if (translate!=null)
            entity.setName(translate.getName());

        return entity;
    }

    @Inject
    private Lang defLang;
}
