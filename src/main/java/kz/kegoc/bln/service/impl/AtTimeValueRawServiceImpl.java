package kz.kegoc.bln.service.impl;

import kz.kegoc.bln.imp.raw.AtTimeValueRaw;
import kz.kegoc.bln.repository.MeteringValueRepository;
import kz.kegoc.bln.service.AbstractMeteringValueService;
import kz.kegoc.bln.service.MeteringValueService;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.util.List;

@Stateless
public class AtTimeValueRawServiceImpl
    extends AbstractMeteringValueService<AtTimeValueRaw>
        implements MeteringValueService<AtTimeValueRaw> {

	@Inject
    public AtTimeValueRawServiceImpl(MeteringValueRepository<AtTimeValueRaw> repository, Validator validator) {
        super(repository, validator);
    }

    @Override
    public void saveAll(List<AtTimeValueRaw> list) {
        super.saveAll(list);
    }
}
