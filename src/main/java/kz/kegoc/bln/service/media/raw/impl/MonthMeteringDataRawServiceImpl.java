package kz.kegoc.bln.service.media.raw.impl;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import kz.kegoc.bln.entity.media.raw.MonthMeteringDataRaw;
import kz.kegoc.bln.repository.common.Repository;
import kz.kegoc.bln.repository.media.raw.MonthMeteringDataRawRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.raw.MonthMeteringDataRawService;

@Stateless
public class MonthMeteringDataRawServiceImpl extends AbstractEntityService<MonthMeteringDataRaw> implements MonthMeteringDataRawService {
   
	@Inject
    public MonthMeteringDataRawServiceImpl(Repository<MonthMeteringDataRaw> repository, Validator validator) {
        super(repository, validator);
    }

	public void saveAll(List<MonthMeteringDataRaw> list) {
		MonthMeteringDataRawRepository monthRepository = (MonthMeteringDataRawRepository) repository;
		
    	list.stream().forEach(m -> {
    		MonthMeteringDataRaw h = monthRepository.selectByEntity(m);
    		if (h==null) 
    			create(m);
    		else {
    			m.setId(h.getId());
    			m.setCreateDate(h.getCreateDate());
    			update(m);
    		}
        });		
	}	
}
