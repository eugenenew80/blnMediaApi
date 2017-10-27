package kz.kegoc.bln.service.media.impl;

import kz.kegoc.bln.entity.media.GroupMeteringPoint;
import kz.kegoc.bln.repository.media.GroupMeteringPointRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.GroupMeteringPointService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

@Stateless
public class GroupMeteringPointServiceImpl extends AbstractEntityService<GroupMeteringPoint> implements GroupMeteringPointService {

	@Inject
    public GroupMeteringPointServiceImpl(GroupMeteringPointRepository repository, Validator validator) {
        super(repository, validator);
    }
}
