package kz.kegoc.bln.repository.raw.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import kz.kegoc.bln.entity.media.LoadMeteringInfo;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.raw.LoadMeteringInfoRepository;

@Stateless
public class LastLoadMeteringDataRepositoryImpl extends AbstractRepository<LoadMeteringInfo> implements LoadMeteringInfoRepository {
	public LastLoadMeteringDataRepositoryImpl() { setClazz(LoadMeteringInfo.class); }

	public LastLoadMeteringDataRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
