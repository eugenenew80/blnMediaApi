package kz.kegoc.bln.repository.media.data.impl;

import javax.ejb.Stateless;

import kz.kegoc.bln.entity.media.data.MonthMeteringFlow;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.data.MeteringDataRepository;

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
