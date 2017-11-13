package kz.kegoc.bln.service.dict.impl;

import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.repository.common.Repository;
import kz.kegoc.bln.repository.dict.MeteringPointRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.dict.MeteringPointService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;


@Stateless
public class MeteringPointServiceImpl extends AbstractEntityService<MeteringPoint>
        implements MeteringPointService {
    
	@Inject
    public MeteringPointServiceImpl(MeteringPointRepository repository, Validator validator) {
        super(repository, validator);
        this.meteringPointRepository = repository;
    }

    public MeteringPoint findByExternalCode(String externalCode) {
        return meteringPointRepository.selectByExternalCode(externalCode);
    }

    private MeteringPointRepository meteringPointRepository;
}
