package kz.kegoc.bln.service.media.raw.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import kz.kegoc.bln.entity.media.raw.DayMeteringBalanceRaw;
import kz.kegoc.bln.repository.media.raw.MeteringDataRawRepository;
import kz.kegoc.bln.service.media.raw.AbstractMeteringDataRawService;
import kz.kegoc.bln.service.media.raw.MeteringDataRawService;

@Stateless
public class DayMeteringBalanceRawServiceImpl
    extends AbstractMeteringDataRawService<DayMeteringBalanceRaw>
        implements MeteringDataRawService<DayMeteringBalanceRaw> {

	@Inject
    public DayMeteringBalanceRawServiceImpl(MeteringDataRawRepository<DayMeteringBalanceRaw> repository, Validator validator) {
        super(repository, validator);
    }
}
