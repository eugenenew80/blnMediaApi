package kz.kegoc.bln.service.data.impl;

import kz.kegoc.bln.entity.data.WorkListLine;
import kz.kegoc.bln.repository.data.WorkListLineRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.data.WorkListLineService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;


@Stateless
public class WorkListLineServiceImpl extends AbstractEntityService<WorkListLine> implements WorkListLineService {

	@Inject
    public WorkListLineServiceImpl(WorkListLineRepository repository, Validator validator) {
        super(repository, validator);
        this.repository = repository;
    }

	private WorkListLineRepository repository;
}
