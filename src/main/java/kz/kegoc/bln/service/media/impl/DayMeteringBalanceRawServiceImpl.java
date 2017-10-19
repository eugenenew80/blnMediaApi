package kz.kegoc.bln.service.media.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import kz.kegoc.bln.entity.media.DayMeteringBalanceRaw;
import kz.kegoc.bln.repository.media.MeteringDataRepository;
import kz.kegoc.bln.service.media.AbstractMeteringDataService;
import kz.kegoc.bln.service.media.MeteringDataService;

@Stateless
public class DayMeteringBalanceRawServiceImpl
    extends AbstractMeteringDataService<DayMeteringBalanceRaw>
        implements MeteringDataService<DayMeteringBalanceRaw> {

	@Inject
    public DayMeteringBalanceRawServiceImpl(MeteringDataRepository<DayMeteringBalanceRaw> repository, Validator validator) {
        super(repository, validator);
    }
}
