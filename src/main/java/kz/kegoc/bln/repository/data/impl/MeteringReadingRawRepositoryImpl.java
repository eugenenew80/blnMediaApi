package kz.kegoc.bln.repository.data.impl;

import kz.kegoc.bln.entity.data.MeteringReadingRaw;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.data.MeteringDataRepository;
import javax.ejb.Stateless;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class MeteringReadingRawRepositoryImpl
	extends AbstractRepository<MeteringReadingRaw>
		implements MeteringDataRepository<MeteringReadingRaw> {

	public MeteringReadingRaw selectByEntity(MeteringReadingRaw entity) {
		return
			getEntityManager().createNamedQuery("MeteringReadingRaw.findByEntity", MeteringReadingRaw.class)
				.setParameter("sourceMeteringPointCode", entity.getSourceMeteringPointCode())
				.setParameter("meteringDate", 			entity.getMeteringDate())
				.setParameter("sourceUnitCode", 			entity.getSourceUnitCode())
				.setParameter("sourceSystemCode", 		entity.getSourceSystemCode())
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
