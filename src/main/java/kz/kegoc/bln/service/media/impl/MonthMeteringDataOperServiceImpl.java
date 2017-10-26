package kz.kegoc.bln.service.media.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import kz.kegoc.bln.entity.media.month.MonthMeteringDataOper;
import kz.kegoc.bln.repository.media.MonthMeteringDataOperRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.MonthMeteringDataOperService;

@Stateless
public class MonthMeteringDataOperServiceImpl extends AbstractEntityService<MonthMeteringDataOper> implements MonthMeteringDataOperService {
    
	@Inject
    public MonthMeteringDataOperServiceImpl(MonthMeteringDataOperRepository repository, Validator validator) {
        super(repository, validator);
    }
}
