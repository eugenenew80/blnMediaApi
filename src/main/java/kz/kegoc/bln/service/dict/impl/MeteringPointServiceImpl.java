package kz.kegoc.bln.service.dict.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.repository.common.Repository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.dict.MeteringPointService;


@Stateless
public class MeteringPointServiceImpl extends AbstractEntityService<MeteringPoint> implements MeteringPointService {
    
	@Inject
    public MeteringPointServiceImpl(Repository<MeteringPoint> repository, Validator validator) {
        super(repository, validator);
    }
}
