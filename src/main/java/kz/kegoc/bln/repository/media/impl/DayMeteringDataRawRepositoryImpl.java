package kz.kegoc.bln.repository.media.impl;

import javax.ejb.Stateless;

import kz.kegoc.bln.entity.media.day.DayMeteringDataRaw;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.MeteringDataRepository;

@Stateless
public class DayMeteringDataRawRepositoryImpl
	extends AbstractRepository<DayMeteringDataRaw>
		implements MeteringDataRepository<DayMeteringDataRaw> {

	public DayMeteringDataRaw selectByEntity(DayMeteringDataRaw entity) {
		return null;
	}
}
