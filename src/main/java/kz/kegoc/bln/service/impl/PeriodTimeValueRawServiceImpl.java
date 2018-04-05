package kz.kegoc.bln.service.impl;

import kz.kegoc.bln.common.service.AbstractEntityService;
import kz.kegoc.bln.imp.raw.PeriodTimeValueRaw;
import kz.kegoc.bln.repository.PeriodTimeValueRawRepository;
import kz.kegoc.bln.service.PeriodTimeValueRawService;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.util.List;

@Stateless
public class PeriodTimeValueRawServiceImpl
    extends AbstractEntityService<PeriodTimeValueRaw>
        implements PeriodTimeValueRawService {

	@Inject
    public PeriodTimeValueRawServiceImpl(PeriodTimeValueRawRepository repository, Validator validator) {
        super(repository, validator);
        this.periodTimeValueRawRepository = repository;
    }

    @Override
    public void saveAll(List<PeriodTimeValueRaw> list) {
        periodTimeValueRawRepository.saveAll(list);
    }

    PeriodTimeValueRawRepository periodTimeValueRawRepository;
}
