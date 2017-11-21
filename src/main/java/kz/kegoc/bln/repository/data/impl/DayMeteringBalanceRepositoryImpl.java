package kz.kegoc.bln.repository.data.impl;

import javax.ejb.Stateless;

import kz.kegoc.bln.entity.common.DataStatus;
import kz.kegoc.bln.entity.data.DayMeteringBalance;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.data.MeteringDataRepository;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class DayMeteringBalanceRepositoryImpl
	extends AbstractRepository<DayMeteringBalance>
		implements MeteringDataRepository<DayMeteringBalance> {

	public DayMeteringBalanceRepositoryImpl() {
		setClazz(DayMeteringBalance.class);
	}

	public DayMeteringBalance selectByEntity(DayMeteringBalance entity) {
		return
			getEntityManager().createNamedQuery("DayMeteringBalance.findByEntity", DayMeteringBalance.class)
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
			getEntityManager().createNamedQuery("DayMeteringBalance.findReadyData", DayMeteringBalance.class)
				.setParameter("meteringPointId", meteringPointId)
				.setParameter("meteringDate", 	meteringDate)
				.setParameter("paramCode", 		paramCode)
				.setParameter("status", 			DataStatus.OK)
			.getResultList();
	}
}
