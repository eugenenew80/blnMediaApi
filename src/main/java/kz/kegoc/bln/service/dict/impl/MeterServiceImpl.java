package kz.kegoc.bln.service.dict.impl;

import kz.kegoc.bln.entity.dict.Meter;
import kz.kegoc.bln.repository.dict.MeterRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.dict.MeterService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;


@Stateless
public class MeterServiceImpl extends AbstractEntityService<Meter>
        implements MeterService {

	@Inject
    public MeterServiceImpl(MeterRepository repository, Validator validator) {
        super(repository, validator);
    }
}
