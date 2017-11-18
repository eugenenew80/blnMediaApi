package kz.kegoc.bln.service.media.raw.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import kz.kegoc.bln.entity.media.raw.MonthMeteringFlowRaw;
import kz.kegoc.bln.repository.media.raw.MeteringDataRawRepository;
import kz.kegoc.bln.service.media.raw.AbstractMeteringDataRawService;
import kz.kegoc.bln.service.media.raw.MeteringDataRawService;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class MonthMeteringFlowRawServiceImpl
	extends AbstractMeteringDataRawService<MonthMeteringFlowRaw>
		implements MeteringDataRawService<MonthMeteringFlowRaw> {
   
	@Inject
    public MonthMeteringFlowRawServiceImpl(MeteringDataRawRepository<MonthMeteringFlowRaw> repository, Validator validator) {
		super(repository, validator);
	}

	public MonthMeteringFlowRaw findByEntity(MonthMeteringFlowRaw entity) {
		return null;
	}

	public List<MonthMeteringFlowRaw> findReadyData(Long meteringPointId, LocalDateTime meteringDate, String paramCode) {
		return null;
	}
}
