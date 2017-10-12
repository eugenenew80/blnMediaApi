package kz.kegoc.bln.repository.media.raw.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import kz.kegoc.bln.entity.media.raw.LoadMeteringInfo;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.raw.LoadMeteringInfoRepository;

@Stateless
public class LoadMeteringInfoRepositoryImpl extends AbstractRepository<LoadMeteringInfo> implements LoadMeteringInfoRepository {
	public LoadMeteringInfoRepositoryImpl() { setClazz(LoadMeteringInfo.class); }

	public LoadMeteringInfoRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}