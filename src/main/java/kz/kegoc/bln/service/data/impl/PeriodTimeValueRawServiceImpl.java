package kz.kegoc.bln.service.data.impl;

import kz.kegoc.bln.imp.raw.PeriodTimeValueRaw;
import kz.kegoc.bln.repository.data.MeteringValueRepository;
import kz.kegoc.bln.service.data.AbstractMeteringValueService;
import kz.kegoc.bln.service.data.MeteringValueService;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.util.List;

@Stateless
public class PeriodTimeValueRawServiceImpl
    extends AbstractMeteringValueService<PeriodTimeValueRaw>
        implements MeteringValueService<PeriodTimeValueRaw> {

	@Inject
    public PeriodTimeValueRawServiceImpl(MeteringValueRepository<PeriodTimeValueRaw> repository, Validator validator) {
        super(repository, validator);
    }

    @Override
    public void saveAll(List<PeriodTimeValueRaw> list) {
        super.saveAll(list);
    }
}
