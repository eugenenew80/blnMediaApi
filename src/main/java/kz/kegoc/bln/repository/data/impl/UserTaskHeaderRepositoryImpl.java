package kz.kegoc.bln.repository.data.impl;

import kz.kegoc.bln.entity.data.UserTaskHeader;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.data.UserTaskHeaderRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
public class UserTaskHeaderRepositoryImpl extends AbstractRepository<UserTaskHeader> implements UserTaskHeaderRepository {
	public UserTaskHeaderRepositoryImpl() { setClazz(UserTaskHeader.class); }

	public UserTaskHeaderRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
