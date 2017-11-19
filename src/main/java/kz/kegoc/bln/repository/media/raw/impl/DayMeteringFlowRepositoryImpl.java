package kz.kegoc.bln.repository.media.raw.impl;

import javax.ejb.Stateless;

import kz.kegoc.bln.entity.media.raw.DayMeteringFlow;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.raw.MeteringDataRepository;

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
