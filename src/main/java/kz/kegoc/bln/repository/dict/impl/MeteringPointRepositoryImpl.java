package kz.kegoc.bln.repository.dict.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.dict.MeteringPointRepository;

@Stateless
public class MeteringPointRepositoryImpl extends AbstractRepository<MeteringPoint> implements MeteringPointRepository {
	public MeteringPointRepositoryImpl() { setClazz(MeteringPoint.class); }

	public MeteringPointRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
