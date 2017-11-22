package kz.kegoc.bln.service.doc.impl;

import kz.kegoc.bln.entity.doc.Group;
import kz.kegoc.bln.filter.Filter;
import kz.kegoc.bln.repository.doc.GroupRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.doc.GroupService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

@Stateless
public class GroupServiceImpl extends AbstractEntityService<Group>
        implements GroupService {

	@Inject
    public GroupServiceImpl(GroupRepository repository, Validator validator, Filter<Group> prePersistFilter) {
        super(repository, validator, prePersistFilter);
    }
}
