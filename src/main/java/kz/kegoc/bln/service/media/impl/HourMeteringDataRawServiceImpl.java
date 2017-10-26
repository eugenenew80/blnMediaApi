package kz.kegoc.bln.service.media.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import kz.kegoc.bln.entity.media.hour.HourMeteringDataRaw;
import kz.kegoc.bln.repository.media.MeteringDataRawRepository;
import kz.kegoc.bln.service.media.AbstractMeteringDataService;
import kz.kegoc.bln.service.media.MeteringDataService;

@Stateless
public class HourMeteringDataRawServiceImpl
    extends AbstractMeteringDataService<HourMeteringDataRaw>
        implements MeteringDataService<HourMeteringDataRaw> {
   
	@Inject
    public HourMeteringDataRawServiceImpl(MeteringDataRawRepository<HourMeteringDataRaw> repository, Validator validator) {
        super(repository, validator);
    }
}
