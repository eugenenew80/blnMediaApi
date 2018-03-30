package kz.kegoc.bln.service.impl;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.validation.Validator;
import kz.kegoc.bln.entity.media.LastLoadInfo;
import kz.kegoc.bln.repository.LastLoadInfoRepository;
import kz.kegoc.bln.common.service.AbstractEntityService;
import kz.kegoc.bln.service.LastLoadInfoService;

@Stateless
public class LastLoadInfoServiceImpl extends AbstractEntityService<LastLoadInfo> implements LastLoadInfoService {
    
	@Inject
    public LastLoadInfoServiceImpl(LastLoadInfoRepository repository, Validator validator) {
        super(repository, validator);
        this.lastLoadInfoRepository = repository;
    }

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void atUpdateLastDate(Long batchId) {
        lastLoadInfoRepository.atUpdateLastDate(batchId);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void ptUpdateLastDate(Long batchId) {
        lastLoadInfoRepository.ptUpdateLastDate(batchId);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void atLoad(Long batchId) {
        lastLoadInfoRepository.atLoad(batchId);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void ptLoad(Long batchId) {
        lastLoadInfoRepository.ptLoad(batchId);
	}

	private LastLoadInfoRepository lastLoadInfoRepository;
}
