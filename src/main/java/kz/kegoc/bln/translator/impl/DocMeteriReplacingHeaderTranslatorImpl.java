package kz.kegoc.bln.translator.impl;

import kz.kegoc.bln.entity.common.Lang;
import kz.kegoc.bln.entity.doc.DocMeterReplacingHeader;
import kz.kegoc.bln.entity.doc.translate.DocMeterReplacingHeaderTranslate;
import kz.kegoc.bln.translator.Translator;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class DocMeteriReplacingHeaderTranslatorImpl implements Translator<DocMeterReplacingHeader> {
    public DocMeterReplacingHeader translate(DocMeterReplacingHeader entity, Lang lang) {
        entity.setLang(lang);

        if (entity.getTranslations()==null)
            return entity;

        DocMeterReplacingHeaderTranslate translate = entity.getTranslations().get(lang);
        if (translate==null)
            translate = entity.getTranslations().get(defLang);

        if (translate!=null)
            entity.setName(translate.getName());

        return entity;
    }

    @Inject
    private Lang defLang;
}
