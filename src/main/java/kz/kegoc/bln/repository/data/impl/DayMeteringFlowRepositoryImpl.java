package kz.kegoc.bln.repository.data.impl;

import javax.ejb.Stateless;

import kz.kegoc.bln.entity.data.DayMeteringFlow;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.data.MeteringDataRepository;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class DayMeteringFlowRepositoryImpl
	extends AbstractRepository<DayMeteringFlow>
		implements MeteringDataRepository<DayMeteringFlow> {

	public DayMeteringFlow selectByEntity(DayMeteringFlow entity) {
		return null;
	}

	public List selectReadyData(Long meteringPointId, LocalDateTime meteringDate, String paramCode) {
		return null;
	}
}
