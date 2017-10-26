package kz.kegoc.bln.service.media.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import kz.kegoc.bln.entity.media.day.DayMeteringDataRaw;
import kz.kegoc.bln.repository.media.MeteringDataRawRepository;
import kz.kegoc.bln.service.media.AbstractMeteringDataService;
import kz.kegoc.bln.service.media.MeteringDataService;

@Stateless
public class DayMeteringDataRawServiceImpl
    extends AbstractMeteringDataService<DayMeteringDataRaw>
        implements MeteringDataService<DayMeteringDataRaw> {

	@Inject
    public DayMeteringDataRawServiceImpl(MeteringDataRawRepository<DayMeteringDataRaw> repository, Validator validator) {
        super(repository, validator);
    }
}
