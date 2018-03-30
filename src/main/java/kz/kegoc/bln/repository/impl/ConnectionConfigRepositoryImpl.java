package kz.kegoc.bln.repository.impl;

import kz.kegoc.bln.entity.media.ConnectionConfig;
import kz.kegoc.bln.common.repository.AbstractRepository;
import kz.kegoc.bln.repository.ConnectionConfigRepository;
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
