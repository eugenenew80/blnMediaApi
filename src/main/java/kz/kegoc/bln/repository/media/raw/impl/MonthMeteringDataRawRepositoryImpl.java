package kz.kegoc.bln.repository.media.raw.impl;

import javax.ejb.Stateless;

import kz.kegoc.bln.entity.media.raw.MonthMeteringDataRaw;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.raw.MeteringDataRawRepository;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class MonthMeteringDataRawRepositoryImpl
	extends AbstractRepository<MonthMeteringDataRaw>
		implements MeteringDataRawRepository<MonthMeteringDataRaw> {

	public MonthMeteringDataRaw selectByEntity(MonthMeteringDataRaw entity) {
		return null;
	}

	public List selectReadyData(Long meteringPointId, LocalDateTime meteringDate, String paramCode) {
		return null;
	}
}
