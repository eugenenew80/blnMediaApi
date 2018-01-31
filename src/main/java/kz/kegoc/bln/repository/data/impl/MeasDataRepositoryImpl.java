package kz.kegoc.bln.repository.data.impl;

import kz.kegoc.bln.entity.data.MeasData;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.data.MeteringDataRepository;

import javax.ejb.Stateless;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class MeasDataRepositoryImpl
	extends AbstractRepository<MeasData>
		implements MeteringDataRepository<MeasData> {

	public MeasData selectByEntity(MeasData entity) {
		return
			getEntityManager().createNamedQuery("MeasData.findByEntity", MeasData.class)
				.setParameter("sourceMeteringPointCode", entity.getSourceMeteringPointCode())
				.setParameter("measDate", 				entity.getMeasDate())
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
