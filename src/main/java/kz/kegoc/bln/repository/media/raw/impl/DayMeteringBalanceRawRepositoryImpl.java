package kz.kegoc.bln.repository.media.raw.impl;

import javax.ejb.Stateless;

import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.raw.DayMeteringBalanceRaw;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.raw.MeteringDataRawRepository;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class DayMeteringBalanceRawRepositoryImpl
	extends AbstractRepository<DayMeteringBalanceRaw>
		implements MeteringDataRawRepository<DayMeteringBalanceRaw> {

	public DayMeteringBalanceRawRepositoryImpl() {
		setClazz(DayMeteringBalanceRaw.class);
	}

	public DayMeteringBalanceRaw selectByEntity(DayMeteringBalanceRaw entity) {
		return
			getEntityManager().createNamedQuery("DayMeteringBalanceRaw.findByEntity", DayMeteringBalanceRaw.class)
				.setParameter("externalCode", 	entity.getExternalCode())
				.setParameter("meteringDate", 	entity.getMeteringDate())
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
		return
			getEntityManager().createNamedQuery("DayMeteringBalanceRaw.findReadyData", DayMeteringBalanceRaw.class)
				.setParameter("meteringPointId", meteringPointId)
				.setParameter("meteringDate", 	meteringDate)
				.setParameter("paramCode", 		paramCode)
				.setParameter("status", 			DataStatus.OK)
			.getResultList();
	}
}
