package kz.kegoc.bln.service.media.impl.raw;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import kz.kegoc.bln.entity.media.raw.HourMeteringDataRaw;
import kz.kegoc.bln.repository.media.raw.MeteringDataRepository;
import kz.kegoc.bln.service.media.AbstractMeteringDataService;
import kz.kegoc.bln.service.media.MeteringDataService;

@Stateless
public class HourMeteringDataRawServiceImpl
    extends AbstractMeteringDataService<HourMeteringDataRaw>
        implements MeteringDataService<HourMeteringDataRaw> {
   
	@Inject
    public HourMeteringDataRawServiceImpl(MeteringDataRepository<HourMeteringDataRaw> repository, Validator validator) {
        super(repository, validator);
    }
}
