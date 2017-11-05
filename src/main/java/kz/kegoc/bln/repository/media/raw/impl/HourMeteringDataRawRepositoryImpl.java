package kz.kegoc.bln.repository.media.raw.impl;

import kz.kegoc.bln.entity.media.raw.HourMeteringDataRaw;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.raw.MeteringDataRawRepository;

import javax.ejb.Stateless;

@Stateless
public class HourMeteringDataRawRepositoryImpl
	extends AbstractRepository<HourMeteringDataRaw>
		implements MeteringDataRawRepository<HourMeteringDataRaw> {

	public HourMeteringDataRaw selectByEntity(HourMeteringDataRaw entity) {
		return
			getEntityManager().createNamedQuery("HourMeteringDataRaw.findByEntity", HourMeteringDataRaw.class)
				.setParameter("externalCode", 	entity.getExternalCode())
				.setParameter("meteringDate", 	entity.getMeteringDate())
				.setParameter("hour", 			entity.getHour())
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
