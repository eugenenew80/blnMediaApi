package kz.kegoc.bln.repository.impl;

import kz.kegoc.bln.entity.media.Batch;
import kz.kegoc.bln.common.repository.AbstractRepository;
import kz.kegoc.bln.repository.BatchRepository;

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
