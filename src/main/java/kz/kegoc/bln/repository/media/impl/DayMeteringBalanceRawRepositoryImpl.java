package kz.kegoc.bln.repository.media.impl;

import javax.ejb.Stateless;

import kz.kegoc.bln.entity.media.day.DayMeteringBalanceRaw;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.MeteringDataRepository;

@Stateless
public class DayMeteringBalanceRawRepositoryImpl
	extends AbstractRepository<DayMeteringBalanceRaw>
		implements MeteringDataRepository<DayMeteringBalanceRaw> {

	public DayMeteringBalanceRaw selectByEntity(DayMeteringBalanceRaw entity) {
		return
			getEntityManager().createNamedQuery("DayMeteringBalanceRaw.findByEntity", DayMeteringBalanceRaw.class)
				.setParameter("externalCode", 	entity.getExternalCode())
				.setParameter("meteringDate", 	entity.getMeteringDate())
				.setParameter("unitCode", 		entity.getUnitCode())
				.setParameter("dataSourceCode", entity.getDataSourceCode())
				.setParameter("paramCode", 		entity.getParamCode())
				.setParameter("wayEntering", 	entity.getWayEntering())
				.setParameter("status", 		entity.getStatus())
			.getResultList()
				.stream()
				.findFirst()
				.orElse(null);
	}
}
