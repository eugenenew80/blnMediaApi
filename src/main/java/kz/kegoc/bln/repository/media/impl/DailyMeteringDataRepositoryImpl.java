package kz.kegoc.bln.repository.media.impl;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import kz.kegoc.bln.entity.media.DailyMeteringData;
import kz.kegoc.bln.repository.media.DailyMeteringDataRepository;

@Stateless
@Transactional
public class DailyMeteringDataRepositoryImpl implements DailyMeteringDataRepository {

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public DailyMeteringData insert(DailyMeteringData entity) {
		em.persist(entity);
		return entity;
	}

	
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void insertAll(List<DailyMeteringData> list) {
	}
	
	
	@PersistenceContext(unitName = "bln")
	private EntityManager em;
}
