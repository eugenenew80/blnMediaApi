package kz.kegoc.bln.service.data.impl;

import kz.kegoc.bln.entity.data.Parameter;
import kz.kegoc.bln.repository.data.ParameterRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.data.ParameterService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;


@Stateless
public class ParameterServiceImpl extends AbstractEntityService<Parameter> implements ParameterService {

	@Inject
    public ParameterServiceImpl(ParameterRepository repository, Validator validator) {
        super(repository, validator);
        this.repository = repository;
    }

	private ParameterRepository repository;
}
