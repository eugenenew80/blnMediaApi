package kz.kegoc.bln.service.media.oper.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import kz.kegoc.bln.entity.media.oper.DayMeteringDataOper;
import kz.kegoc.bln.repository.media.oper.DayMeteringDataOperRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.oper.DayMeteringDataOperService;

@Stateless
public class DayMeteringDataOperServiceImpl extends AbstractEntityService<DayMeteringDataOper>
        implements DayMeteringDataOperService {
    
	@Inject
    public DayMeteringDataOperServiceImpl(DayMeteringDataOperRepository repository, Validator validator) {
        super(repository, validator);
        this.dayMeteringDataOperRepository = repository;
    }

	public List<DayMeteringDataOper> findByGroup(Long groupId) {
		return dayMeteringDataOperRepository.selectByGroup(groupId);
	}
	
	private DayMeteringDataOperRepository dayMeteringDataOperRepository;
}
