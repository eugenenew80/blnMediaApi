package kz.kegoc.bln.repository.media.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import kz.kegoc.bln.entity.media.day.DayMeteringDataOper;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.DayMeteringDataOperRepository;

@Stateless
public class DayMeteringDataOperRepositoryImpl extends AbstractRepository<DayMeteringDataOper> implements DayMeteringDataOperRepository {
	public DayMeteringDataOperRepositoryImpl() { setClazz(DayMeteringDataOper.class); }

	public DayMeteringDataOperRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
