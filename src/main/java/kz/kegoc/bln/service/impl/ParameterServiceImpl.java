package kz.kegoc.bln.service.impl;

import kz.kegoc.bln.entity.media.Parameter;
import kz.kegoc.bln.repository.ParameterRepository;
import kz.kegoc.bln.common.service.AbstractEntityService;
import kz.kegoc.bln.service.ParameterService;

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
