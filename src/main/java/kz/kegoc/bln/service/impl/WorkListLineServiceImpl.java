package kz.kegoc.bln.service.impl;

import kz.kegoc.bln.entity.media.WorkListLine;
import kz.kegoc.bln.repository.WorkListLineRepository;
import kz.kegoc.bln.common.service.AbstractEntityService;
import kz.kegoc.bln.service.WorkListLineService;

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
