package kz.kegoc.bln.repository.media.oper.impl;

import kz.kegoc.bln.entity.media.oper.TemplateMeteringReading;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.oper.TemplateMeteringReadingRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
public class TemplateMeteringReadingRepositoryImpl
		extends AbstractRepository<TemplateMeteringReading>
				implements TemplateMeteringReadingRepository {

	public TemplateMeteringReadingRepositoryImpl() { setClazz(TemplateMeteringReading.class); }

	public TemplateMeteringReadingRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
