package kz.kegoc.bln.mapper.impl;

import kz.kegoc.bln.entity.media.Lang;
import kz.kegoc.bln.entity.media.doc.Group;
import kz.kegoc.bln.entity.media.doc.translate.GroupTranslate;
import kz.kegoc.bln.mapper.EntityMapper;
import kz.kegoc.bln.service.media.doc.GroupService;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.HashMap;

@Stateless
public class GroupMapperImpl implements EntityMapper<Group> {
    public Group map(Group entity) {
        if (entity.getId()!=null) {
            Group curEntity = groupService.findById(entity.getId());

            if (entity.getTranslations()==null)
                entity.setTranslations(curEntity.getTranslations());
        }

        return translate(entity);
    }

    private Group translate(Group entity) {
        Lang lang = entity.getLang();
        if (lang==null)
            lang = defLang;

        if (entity.getTranslations()==null)
            entity.setTranslations(new HashMap<>());

        GroupTranslate curTranslate = entity.getTranslations()
            .get(lang);

        if (curTranslate!=null)
            curTranslate.setName(entity.getName());
        else {
            GroupTranslate translate = new GroupTranslate();
            translate.setLang(lang);
            translate.setGroup(entity);
            translate.setName(entity.getName());
            entity.getTranslations().put(lang, translate);
        }

        return entity;
    }


    @Inject
    private GroupService groupService;

    @Inject
    private Lang defLang;
}
