package kz.kegoc.bln.repository.data.impl;

import kz.kegoc.bln.entity.data.WorkListLine;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.data.WorkListLineRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
public class WorkListLineRepositoryImpl extends AbstractRepository<WorkListLine> implements WorkListLineRepository {
	public WorkListLineRepositoryImpl() { setClazz(WorkListLine.class); }

	public WorkListLineRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
