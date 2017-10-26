package kz.kegoc.bln.service.media.impl;

import kz.kegoc.bln.entity.media.Group;
import kz.kegoc.bln.repository.media.GroupRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.GroupService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

@Stateless
public class GroupServiceImpl extends AbstractEntityService<Group> implements GroupService {

	@Inject
    public GroupServiceImpl(GroupRepository repository, Validator validator) {
        super(repository, validator);
    }
}
