package kz.kegoc.bln.repository.impl;

import kz.kegoc.bln.entity.media.WorkListLine;
import kz.kegoc.bln.common.repository.AbstractRepository;
import kz.kegoc.bln.repository.WorkListLineRepository;

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
