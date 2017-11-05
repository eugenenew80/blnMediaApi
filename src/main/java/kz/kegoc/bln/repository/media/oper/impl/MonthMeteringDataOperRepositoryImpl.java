package kz.kegoc.bln.repository.media.oper.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import kz.kegoc.bln.entity.media.oper.MonthMeteringDataOper;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.oper.MonthMeteringDataOperRepository;

@Stateless
public class MonthMeteringDataOperRepositoryImpl
		extends AbstractRepository<MonthMeteringDataOper>
				implements MonthMeteringDataOperRepository {

	public MonthMeteringDataOperRepositoryImpl() { setClazz(MonthMeteringDataOper.class); }

	public MonthMeteringDataOperRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
