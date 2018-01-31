package kz.kegoc.bln.repository.data.impl;

import javax.ejb.Stateless;

import kz.kegoc.bln.entity.common.DataStatus;
import kz.kegoc.bln.entity.data.MeteringReading;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.data.MeteringDataRepository;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class MeteringReadingRepositoryImpl
	extends AbstractRepository<MeteringReading>
		implements MeteringDataRepository<MeteringReading> {

	public MeteringReadingRepositoryImpl() {
		setClazz(MeteringReading.class);
	}

	public MeteringReading selectByEntity(MeteringReading entity) {
		return
			getEntityManager().createNamedQuery("MeteringReading.findByEntity", MeteringReading.class)
				.setParameter("sourceMeteringPointCode", entity.getSourceMeteringPointCode())
				.setParameter("meteringDate", 			entity.getMeteringDate())
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
		return
			getEntityManager().createNamedQuery("MeteringReading.findReadyData", MeteringReading.class)
				.setParameter("meteringPointId", meteringPointId)
				.setParameter("meteringDate", 	meteringDate)
				.setParameter("paramCode", 		paramCode)
				.setParameter("status", 			DataStatus.OK)
			.getResultList();
	}
}
