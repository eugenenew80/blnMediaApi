package kz.kegoc.bln.service.data.impl;

import kz.kegoc.bln.entity.data.Batch;
import kz.kegoc.bln.repository.data.BatchRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.data.BatchService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;


@Stateless
public class BatchServiceImpl extends AbstractEntityService<Batch> implements BatchService {

	@Inject
    public BatchServiceImpl(BatchRepository repository, Validator validator) {
        super(repository, validator);
        this.repository = repository;
    }

	private BatchRepository repository;
}
