package kz.kegoc.bln.repository.media.raw.impl;

import kz.kegoc.bln.entity.media.HourMeteringDataRaw;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.raw.MeteringDataRepository;

import javax.ejb.Stateless;

@Stateless
public class HourMeteringDataRawRepositoryImpl
	extends AbstractRepository<HourMeteringDataRaw>
		implements MeteringDataRepository<HourMeteringDataRaw> {

	public HourMeteringDataRaw selectByEntity(HourMeteringDataRaw entity) {
		return
			getEntityManager().createNamedQuery("HourMeteringDataRaw.findByEntity", HourMeteringDataRaw.class)
				.setParameter("externalCode", 	entity.getExternalCode())
				.setParameter("meteringDate", 	entity.getMeteringDate())
				.setParameter("hour", 			entity.getHour())
				.setParameter("unitCode", 		entity.getUnitCode())
				.setParameter("dataSourceCode", 	entity.getDataSourceCode())
				.setParameter("paramCode", 		entity.getParamCode())
				.setParameter("wayEntering", 		entity.getWayEntering())
				.setParameter("status", 			entity.getStatus())
			.getResultList()
				.stream()
				.findFirst()
				.orElse(null);
	}
}
