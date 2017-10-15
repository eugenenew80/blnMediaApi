package kz.kegoc.bln.repository.media.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import kz.kegoc.bln.entity.media.LoadMeteringInfo;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.LoadMeteringInfoRepository;

@Stateless
public class LoadMeteringInfoRepositoryImpl extends AbstractRepository<LoadMeteringInfo> implements LoadMeteringInfoRepository {
	public LoadMeteringInfoRepositoryImpl() { setClazz(LoadMeteringInfo.class); }

	public LoadMeteringInfoRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
