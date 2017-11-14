package kz.kegoc.bln.service.media.raw.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import kz.kegoc.bln.entity.media.raw.MonthMeteringDataRaw;
import kz.kegoc.bln.repository.media.raw.MeteringDataRawRepository;
import kz.kegoc.bln.service.media.raw.AbstractMeteringDataRawService;
import kz.kegoc.bln.service.media.raw.MeteringDataRawService;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class MonthMeteringDataRawServiceImpl
	extends AbstractMeteringDataRawService<MonthMeteringDataRaw>
		implements MeteringDataRawService<MonthMeteringDataRaw> {
   
	@Inject
    public MonthMeteringDataRawServiceImpl(MeteringDataRawRepository<MonthMeteringDataRaw> repository, Validator validator) {
		super(repository, validator);
	}

	public MonthMeteringDataRaw findByEntity(MonthMeteringDataRaw entity) {
		return null;
	}

	public List<MonthMeteringDataRaw> findReadyData(Long meteringPointId, LocalDateTime meteringDate, String paramCode) {
		return null;
	}
}
