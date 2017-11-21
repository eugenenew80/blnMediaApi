package kz.kegoc.bln.service.doc.impl;

import kz.kegoc.bln.entity.doc.GroupMeteringPoint;
import kz.kegoc.bln.repository.doc.GroupMeteringPointRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.doc.GroupMeteringPointService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

@Stateless
public class GroupMeteringPointServiceImpl
        extends AbstractEntityService<GroupMeteringPoint>
                implements GroupMeteringPointService {

	@Inject
    public GroupMeteringPointServiceImpl(GroupMeteringPointRepository repository, Validator validator) {
        super(repository, validator);
    }
}
