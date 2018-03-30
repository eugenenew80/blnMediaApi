package kz.kegoc.bln.service.impl;

import kz.kegoc.bln.entity.media.Batch;
import kz.kegoc.bln.repository.BatchRepository;
import kz.kegoc.bln.common.service.AbstractEntityService;
import kz.kegoc.bln.service.BatchService;

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
