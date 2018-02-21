package kz.kegoc.bln.service.data.impl;

import kz.kegoc.bln.entity.data.PeriodTimeValue;
import kz.kegoc.bln.repository.data.PeriodTimeValueRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.data.PeriodTimeValueService;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class PeriodTimeValueServiceImpl extends AbstractEntityService<PeriodTimeValue> implements PeriodTimeValueService {

	@Inject
    public PeriodTimeValueServiceImpl(PeriodTimeValueRepository repository, Validator validator) {
        super(repository, validator);
        this.repository = repository;
    }

    public List<PeriodTimeValue> findByExternalCode(String sourceMeteringPointCode, LocalDateTime meteringDateStart, LocalDateTime meteringDateEnd) {
        return repository.selectByExternalCode(sourceMeteringPointCode, meteringDateStart, meteringDateEnd);
    }

    private PeriodTimeValueRepository repository;
}
