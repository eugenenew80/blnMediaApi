package kz.kegoc.bln.repository.data.impl;

import kz.kegoc.bln.entity.data.PeriodTimeValue;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.data.PeriodTimeValueRepository;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class PeriodTimeValueRepositoryImpl extends AbstractRepository<PeriodTimeValue> implements PeriodTimeValueRepository {
	public PeriodTimeValueRepositoryImpl() {
		setClazz(PeriodTimeValue.class);
	}

	public PeriodTimeValueRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}

	public List<PeriodTimeValue> selectByExternalCode(String sourceMeteringPointCode, LocalDateTime meteringDateStart, LocalDateTime meteringDateEnd) {
		return getEntityManager().createNamedQuery("PeriodTimeValue.findByExternalCode", PeriodTimeValue.class)
					.setParameter("sourceMeteringPointCode", sourceMeteringPointCode)
					.setParameter("meteringDateStart", 		meteringDateStart)
					.setParameter("meteringDateEnd", 		meteringDateEnd)
					.getResultList();
	}
}
