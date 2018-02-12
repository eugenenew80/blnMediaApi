package kz.kegoc.bln.repository.data.impl;

import kz.kegoc.bln.entity.data.WorkListHeader;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.data.WorkListHeaderRepository;

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
