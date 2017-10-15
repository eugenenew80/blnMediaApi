package kz.kegoc.bln.repository.media.raw.impl;

import javax.ejb.Stateless;
import kz.kegoc.bln.entity.media.raw.MonthMeteringDataRaw;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.raw.MeteringDataRepository;

@Stateless
public class MonthMeteringDataRawRepositoryImpl
	extends AbstractRepository<MonthMeteringDataRaw>
		implements MeteringDataRepository<MonthMeteringDataRaw> {

	public MonthMeteringDataRaw selectByEntity(MonthMeteringDataRaw entity) {
		return null;
	}
}
