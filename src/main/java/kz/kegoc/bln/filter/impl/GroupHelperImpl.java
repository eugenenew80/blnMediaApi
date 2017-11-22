package kz.kegoc.bln.filter.impl;

import kz.kegoc.bln.entity.common.Lang;
import kz.kegoc.bln.entity.doc.Group;
import kz.kegoc.bln.entity.doc.translate.GroupTranslate;
import kz.kegoc.bln.filter.Filter;
import kz.kegoc.bln.service.doc.GroupService;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.HashMap;

@Stateless
public class GroupHelperImpl implements Filter<Group> {
    public Group filter(Group entity) {
        if (entity.getId()!=null) {
            Group curEntity = groupService.findById(entity.getId());

            if (entity.getMeteringPoints()==null)
                entity.setMeteringPoints(curEntity.getMeteringPoints());

            if (entity.getTranslations()==null)
                entity.setTranslations(curEntity.getTranslations());
        }

        return translate(entity);
    }

    private Group translate(Group entity) {
        Lang lang = entity.getLang()!=null ? entity.getLang() : defLang;
        if (entity.getTranslations()==null)
            entity.setTranslations(new HashMap<>());

        GroupTranslate translate = entity.getTranslations().getOrDefault(lang, new GroupTranslate());
        translate.setLang(lang);
        translate.setGroup(entity);
        translate.setName(entity.getName());
        entity.getTranslations().put(lang, translate);

        return entity;
    }


    @Inject
    private GroupService groupService;

    @Inject
    private Lang defLang;
}
