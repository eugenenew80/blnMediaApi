package kz.kegoc.bln.service.media.raw.impl;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import kz.kegoc.bln.entity.media.raw.DayMeteringDataRaw;
import kz.kegoc.bln.repository.common.Repository;
import kz.kegoc.bln.repository.media.raw.DayMeteringDataRawRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.raw.MeteringDataService;

@Stateless
public class DayMeteringDataRawServiceImpl extends AbstractEntityService<DayMeteringDataRaw> implements MeteringDataService<DayMeteringDataRaw> {
   
	@Inject
    public DayMeteringDataRawServiceImpl(Repository<DayMeteringDataRaw> repository, Validator validator) {
        super(repository, validator);
    }

	public void saveAll(List<DayMeteringDataRaw> list) {
		DayMeteringDataRawRepository dayRepository = (DayMeteringDataRawRepository) repository;
		
    	list.stream().forEach(m -> {
    		DayMeteringDataRaw h = dayRepository.selectByEntity(m);
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
