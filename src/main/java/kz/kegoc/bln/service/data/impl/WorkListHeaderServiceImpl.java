package kz.kegoc.bln.service.data.impl;

import kz.kegoc.bln.entity.data.WorkListHeader;
import kz.kegoc.bln.repository.data.WorkListHeaderRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.data.WorkListHeaderService;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;


@Stateless
public class WorkListHeaderServiceImpl extends AbstractEntityService<WorkListHeader> implements WorkListHeaderService {

	@Inject
    public WorkListHeaderServiceImpl(WorkListHeaderRepository repository, Validator validator) {
        super(repository, validator);
        this.repository = repository;
    }

	private WorkListHeaderRepository repository;
}
