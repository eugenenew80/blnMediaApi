package kz.kegoc.bln.translation.media;

import kz.kegoc.bln.entity.media.oper.DocMeteringReadingHeader;
import kz.kegoc.bln.entity.media.oper.translate.DocMeteringReadingHeaderTranslate;
import kz.kegoc.bln.entity.media.raw.Lang;
import kz.kegoc.bln.translation.Translator;

import javax.ejb.Stateless;

@Stateless
public class DocMeteringReadingHeaderTranslator implements Translator<DocMeteringReadingHeader> {
    public DocMeteringReadingHeader translate(DocMeteringReadingHeader entity, Lang defLang) {
        Lang lang = entity.getLang();
        if (lang==null)
            lang = defLang;

        DocMeteringReadingHeaderTranslate translate = entity.getTranslations().get(lang);
        if (translate==null)
            translate = entity.getTranslations().get(defLang);

        if (translate!=null) {
            entity.setLang(translate.getLang());
            entity.setName(translate.getName());
        }

        return entity;
    }
}
