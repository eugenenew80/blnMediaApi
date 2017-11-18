package kz.kegoc.bln.repository.media.raw.impl;

import javax.ejb.Stateless;

import kz.kegoc.bln.entity.media.raw.MonthMeteringFlowRaw;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.raw.MeteringDataRawRepository;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class MonthMeteringFlowRawRepositoryImpl
	extends AbstractRepository<MonthMeteringFlowRaw>
		implements MeteringDataRawRepository<MonthMeteringFlowRaw> {

	public MonthMeteringFlowRaw selectByEntity(MonthMeteringFlowRaw entity) {
		return null;
	}

	public List selectReadyData(Long meteringPointId, LocalDateTime meteringDate, String paramCode) {
		return null;
	}
}
