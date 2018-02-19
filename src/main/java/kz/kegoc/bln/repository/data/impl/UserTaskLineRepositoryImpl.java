package kz.kegoc.bln.repository.data.impl;

import kz.kegoc.bln.entity.data.UserTaskLine;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.data.UserTaskLineRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
public class UserTaskLineRepositoryImpl extends AbstractRepository<UserTaskLine> implements UserTaskLineRepository {
	public UserTaskLineRepositoryImpl() { setClazz(UserTaskLine.class); }

	public UserTaskLineRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
