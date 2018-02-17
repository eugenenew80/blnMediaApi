package kz.kegoc.bln.repository.data.impl;

import kz.kegoc.bln.entity.data.Batch;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.data.BatchRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
public class BatchRepositoryImpl extends AbstractRepository<Batch> implements BatchRepository {
	public BatchRepositoryImpl() { setClazz(Batch.class); }

	public BatchRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
