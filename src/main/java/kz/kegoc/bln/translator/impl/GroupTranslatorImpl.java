package kz.kegoc.bln.translator.impl;

import kz.kegoc.bln.entity.media.Lang;
import kz.kegoc.bln.entity.media.doc.Group;
import kz.kegoc.bln.entity.media.doc.translate.GroupTranslate;
import kz.kegoc.bln.translator.Translator;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class GroupTranslatorImpl implements Translator<Group> {
    public Group translate(Group entity, Lang lang) {
        entity.setLang(lang);

        if (entity.getTranslations()==null)
            return entity;

        GroupTranslate translate = entity.getTranslations().get(lang);
        if (translate==null)
            translate = entity.getTranslations().get(defLang);

        if (translate!=null)
            entity.setName(translate.getName());

        return entity;
    }

    @Inject
    private Lang defLang;
}
