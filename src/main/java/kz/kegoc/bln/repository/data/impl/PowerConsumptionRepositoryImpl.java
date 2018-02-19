package kz.kegoc.bln.repository.data.impl;

import kz.kegoc.bln.entity.data.PowerConsumption;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.data.PowerConsumptionRepository;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class PowerConsumptionRepositoryImpl extends AbstractRepository<PowerConsumption> implements PowerConsumptionRepository {
	public PowerConsumptionRepositoryImpl() {
		setClazz(PowerConsumption.class);
	}

	public PowerConsumptionRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}

	public List<PowerConsumption> selectByExternalCode(String externalCode, LocalDateTime meteringDateStart, LocalDateTime meteringDateEnd) {
		return getEntityManager().createNamedQuery("PowerConsumption.findByExternalCode", PowerConsumption.class)
					.setParameter("sourceMeteringPointCode", 	externalCode)
					.setParameter("meteringDateStart", 		meteringDateStart)
					.setParameter("meteringDateEnd", 			meteringDateEnd)
					.getResultList();
	}
}
