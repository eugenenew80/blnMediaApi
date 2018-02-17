package kz.kegoc.bln.service.data.impl;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.validation.Validator;
import kz.kegoc.bln.entity.data.LastLoadInfo;
import kz.kegoc.bln.repository.data.LastLoadInfoRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.data.LastLoadInfoService;

@Stateless
public class LastLoadInfoServiceImpl extends AbstractEntityService<LastLoadInfo> implements LastLoadInfoService {
    
	@Inject
    public LastLoadInfoServiceImpl(LastLoadInfoRepository repository, Validator validator) {
        super(repository, validator);
        this.lastLoadInfoRepository = repository;
    }

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void mrUpdateLastDate(Long batchId) {
        lastLoadInfoRepository.mrUpdateLastDate(batchId);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void pcUpdateLastDate(Long batchId) {
        lastLoadInfoRepository.pcUpdateLastDate(batchId);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void mrLoad(Long batchId) {
        lastLoadInfoRepository.mrLoad(batchId);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void pcLoad(Long batchId) {
        lastLoadInfoRepository.pcLoad(batchId);
	}

	private LastLoadInfoRepository lastLoadInfoRepository;
}
