package kz.kegoc.bln.repository.media.data.impl;

import kz.kegoc.bln.entity.media.data.HourMeteringFlow;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.data.MeteringDataRepository;

import javax.ejb.Stateless;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class HourMeteringFlowRepositoryImpl
	extends AbstractRepository<HourMeteringFlow>
		implements MeteringDataRepository<HourMeteringFlow> {

	public HourMeteringFlow selectByEntity(HourMeteringFlow entity) {
		return
			getEntityManager().createNamedQuery("HourMeteringFlow.findByEntity", HourMeteringFlow.class)
				.setParameter("externalCode", 	entity.getExternalCode())
				.setParameter("meteringDate", 	entity.getMeteringDate())
				.setParameter("hour", 			entity.getHour())
				.setParameter("unitCode", 		entity.getUnitCode())
				.setParameter("dataSource", 		entity.getDataSource())
				.setParameter("paramCode", 		entity.getParamCode())
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
