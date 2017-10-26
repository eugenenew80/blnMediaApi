package kz.kegoc.bln.service.media.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import kz.kegoc.bln.entity.media.day.DayMeteringDataOper;
import kz.kegoc.bln.repository.media.DayMeteringDataOperRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.DayMeteringDataOperService;

@Stateless
public class DayMeteringDataOperServiceImpl extends AbstractEntityService<DayMeteringDataOper> implements DayMeteringDataOperService {
    
	@Inject
    public DayMeteringDataOperServiceImpl(DayMeteringDataOperRepository repository, Validator validator) {
        super(repository, validator);
    }
}
