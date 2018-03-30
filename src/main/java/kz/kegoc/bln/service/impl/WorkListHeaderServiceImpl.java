package kz.kegoc.bln.service.impl;

import kz.kegoc.bln.entity.media.WorkListHeader;
import kz.kegoc.bln.repository.WorkListHeaderRepository;
import kz.kegoc.bln.common.service.AbstractEntityService;
import kz.kegoc.bln.service.WorkListHeaderService;
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
