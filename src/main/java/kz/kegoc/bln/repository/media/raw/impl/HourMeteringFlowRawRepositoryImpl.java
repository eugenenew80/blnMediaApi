package kz.kegoc.bln.repository.media.raw.impl;

import kz.kegoc.bln.entity.media.raw.HourMeteringFlowRaw;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.raw.MeteringDataRawRepository;

import javax.ejb.Stateless;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class HourMeteringFlowRawRepositoryImpl
	extends AbstractRepository<HourMeteringFlowRaw>
		implements MeteringDataRawRepository<HourMeteringFlowRaw> {

	public HourMeteringFlowRaw selectByEntity(HourMeteringFlowRaw entity) {
		return
			getEntityManager().createNamedQuery("HourMeteringDataRaw.findByEntity", HourMeteringFlowRaw.class)
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

	public List selectReadyData(Long meteringPointId, LocalDateTime meteringDate, String paramCode) {
		return null;
	}
}
