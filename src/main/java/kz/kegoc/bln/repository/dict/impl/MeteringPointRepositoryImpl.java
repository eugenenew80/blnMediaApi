package kz.kegoc.bln.repository.dict.impl;

import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.dict.MeteringPointRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;


@Stateless
public class MeteringPointRepositoryImpl extends AbstractRepository<MeteringPoint> implements MeteringPointRepository {
	public MeteringPointRepositoryImpl() { setClazz(MeteringPoint.class); }

	public MeteringPointRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}

	public MeteringPoint selectByExternalCode(String externalCode) {
		TypedQuery<MeteringPoint> query = getEntityManager().createNamedQuery( "MeteringPoint.findByExternalCode", MeteringPoint.class);
		query.setParameter("externalCode", externalCode);

		return query.getResultList().stream()
				.findFirst()
				.orElse(null);
	}
}
