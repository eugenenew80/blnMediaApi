package kz.kegoc.bln.service.media.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import kz.kegoc.bln.entity.media.MonthMeteringDataRaw;
import kz.kegoc.bln.repository.media.MeteringDataRepository;
import kz.kegoc.bln.service.media.AbstractMeteringDataService;
import kz.kegoc.bln.service.media.MeteringDataService;

@Stateless
public class MonthMeteringDataRawServiceImpl
	extends AbstractMeteringDataService<MonthMeteringDataRaw>
		implements MeteringDataService<MonthMeteringDataRaw> {
   
	@Inject
    public MonthMeteringDataRawServiceImpl(MeteringDataRepository<MonthMeteringDataRaw> repository, Validator validator) {
		super(repository, validator);
	}
}
