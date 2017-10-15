package kz.kegoc.bln.repository.media.raw.impl;

import javax.ejb.Stateless;
import kz.kegoc.bln.entity.media.raw.DayMeteringDataRaw;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.raw.MeteringDataRepository;

@Stateless
public class DayMeteringDataRawRepositoryImpl
	extends AbstractRepository<DayMeteringDataRaw>
		implements MeteringDataRepository<DayMeteringDataRaw> {

	public DayMeteringDataRaw selectByEntity(DayMeteringDataRaw entity) {
		return null;
	}
}
