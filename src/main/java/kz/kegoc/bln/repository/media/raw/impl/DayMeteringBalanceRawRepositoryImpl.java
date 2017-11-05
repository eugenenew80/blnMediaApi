package kz.kegoc.bln.repository.media.raw.impl;

import javax.ejb.Stateless;

import kz.kegoc.bln.entity.media.raw.DayMeteringBalanceRaw;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.raw.MeteringDataRawRepository;

@Stateless
public class DayMeteringBalanceRawRepositoryImpl
	extends AbstractRepository<DayMeteringBalanceRaw>
		implements MeteringDataRawRepository<DayMeteringBalanceRaw> {

	public DayMeteringBalanceRaw selectByEntity(DayMeteringBalanceRaw entity) {
		return
			getEntityManager().createNamedQuery("DayMeteringBalanceRaw.findByEntity", DayMeteringBalanceRaw.class)
				.setParameter("externalCode", 	entity.getExternalCode())
				.setParameter("meteringDate", 	entity.getMeteringDate())
				.setParameter("unitCode", 		entity.getUnitCode())
				.setParameter("dataSource", 		entity.getDataSource())
				.setParameter("paramCode", 		entity.getParamCode())
				.setParameter("wayEntering", 		entity.getWayEntering())
				.setParameter("status", 			entity.getStatus())
			.getResultList()
				.stream()
				.findFirst()
				.orElse(null);
	}
}
