package kz.kegoc.bln.repository.data.impl;

import javax.ejb.Stateless;

import kz.kegoc.bln.entity.data.MonthMeteringFlow;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.data.MeteringDataRepository;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class MonthMeteringFlowRepositoryImpl
	extends AbstractRepository<MonthMeteringFlow>
		implements MeteringDataRepository<MonthMeteringFlow> {

	public MonthMeteringFlow selectByEntity(MonthMeteringFlow entity) {
		return null;
	}

	public List selectReadyData(Long meteringPointId, LocalDateTime meteringDate, String paramCode) {
		return null;
	}
}
