package kz.kegoc.bln.repository.data.impl;

import kz.kegoc.bln.entity.data.EmcosConfig;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.data.EmcosConfigRepository;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
public class EmcosConfigRepositoryImpl extends AbstractRepository<EmcosConfig> implements EmcosConfigRepository {
	public EmcosConfigRepositoryImpl() { setClazz(EmcosConfig.class); }

	public EmcosConfigRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
