package kz.kegoc.bln.repository.dict.impl;

import kz.kegoc.bln.entity.dict.Meter;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.dict.MeterRepository;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
public class MeterRepositoryImpl extends AbstractRepository<Meter> implements MeterRepository {
	public MeterRepositoryImpl() { setClazz(Meter.class); }

	public MeterRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
