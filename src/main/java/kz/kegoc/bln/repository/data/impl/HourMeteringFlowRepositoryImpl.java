package kz.kegoc.bln.repository.data.impl;

import kz.kegoc.bln.entity.data.HourMeteringFlow;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.data.MeteringDataRepository;

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
				.setParameter("sourceMeteringPointCode", entity.getSourceMeteringPointCode())
				.setParameter("meteringDate", 			entity.getMeteringDate())
				.setParameter("hour", 					entity.getHour())
				.setParameter("sourceUnitCode", 			entity.getSourceUnitCode())
				.setParameter("dataSourceCode", 			entity.getDataSourceCode())
				.setParameter("sourceParamCode", 		entity.getSourceParamCode())
				.setParameter("status", 					entity.getStatus())
			.getResultList()
				.stream()
				.findFirst()
				.orElse(null);
	}

	public List selectReadyData(Long meteringPointId, LocalDateTime meteringDate, String paramCode) {
		return null;
	}
}
