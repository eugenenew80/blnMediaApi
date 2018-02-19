package kz.kegoc.bln.repository.data.impl;

import kz.kegoc.bln.entity.data.ConnectionConfig;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.data.ConnectionConfigRepository;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
public class ConnectionConfigRepositoryImpl extends AbstractRepository<ConnectionConfig> implements ConnectionConfigRepository {
	public ConnectionConfigRepositoryImpl() { setClazz(ConnectionConfig.class); }

	public ConnectionConfigRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
