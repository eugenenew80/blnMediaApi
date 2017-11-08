package kz.kegoc.bln.repository.media.oper.impl;

import java.time.LocalDate;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import kz.kegoc.bln.entity.media.oper.DayMeteringDataOper;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.oper.DayMeteringDataOperRepository;

@Stateless
public class DayMeteringDataOperRepositoryImpl
		extends AbstractRepository<DayMeteringDataOper>
				implements DayMeteringDataOperRepository {

	public DayMeteringDataOperRepositoryImpl() { setClazz(DayMeteringDataOper.class); }

	public DayMeteringDataOperRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}

	public List<DayMeteringDataOper> findByDate(LocalDate operDate) {
		return getEntityManager().createNamedQuery("DocMeteringReadingLine.findByDate", DayMeteringDataOper.class)
					.setParameter("operDate", operDate)
					.getResultList();
	}
}
