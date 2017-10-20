package kz.kegoc.bln.repository.media.impl;

import javax.ejb.Stateless;

import kz.kegoc.bln.entity.media.month.MonthMeteringDataRaw;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.MeteringDataRepository;

@Stateless
public class MonthMeteringDataRawRepositoryImpl
	extends AbstractRepository<MonthMeteringDataRaw>
		implements MeteringDataRepository<MonthMeteringDataRaw> {

	public MonthMeteringDataRaw selectByEntity(MonthMeteringDataRaw entity) {
		return null;
	}
}
