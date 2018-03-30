package kz.kegoc.bln.repository.impl;

import kz.kegoc.bln.entity.media.WorkListHeader;
import kz.kegoc.bln.common.repository.AbstractRepository;
import kz.kegoc.bln.repository.WorkListHeaderRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
public class WorkListHeaderRepositoryImpl extends AbstractRepository<WorkListHeader> implements WorkListHeaderRepository {
	public WorkListHeaderRepositoryImpl() { setClazz(WorkListHeader.class); }

	public WorkListHeaderRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
