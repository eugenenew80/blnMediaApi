package kz.kegoc.bln.service.media.doc.impl;

import kz.kegoc.bln.entity.media.doc.GroupMeteringPoint;
import kz.kegoc.bln.repository.media.doc.GroupMeteringPointRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.doc.GroupMeteringPointService;

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
