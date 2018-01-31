package kz.kegoc.bln.repository.data.impl;

import kz.kegoc.bln.entity.data.MeasDataRaw;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.data.MeteringDataRepository;

import javax.ejb.Stateless;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class MeasDataRawRepositoryImpl
	extends AbstractRepository<MeasDataRaw>
		implements MeteringDataRepository<MeasDataRaw> {

	public MeasDataRaw selectByEntity(MeasDataRaw entity) {
		return
			getEntityManager().createNamedQuery("MeasDataRaw.findByEntity", MeasDataRaw.class)
				.setParameter("sourceMeteringPointCode", entity.getSourceMeteringPointCode())
				.setParameter("measDate", 				entity.getMeasDate())
				.setParameter("sourceUnitCode", 			entity.getSourceUnitCode())
				.setParameter("dataSourceCode", 			entity.getDataSourceCode())
				.setParameter("sourceParamCode", 		entity.getSourceParamCode())
			.getResultList()
				.stream()
				.findFirst()
				.orElse(null);
	}

	public List selectReadyData(Long meteringPointId, LocalDateTime meteringDate, String paramCode) {
		return null;
	}
}
