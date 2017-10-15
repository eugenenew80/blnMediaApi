package kz.kegoc.bln.service.media.raw.impl;

import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import kz.kegoc.bln.entity.media.raw.HourMeteringDataRaw;
import kz.kegoc.bln.repository.common.Repository;
import kz.kegoc.bln.repository.media.raw.HourMeteringDataRawRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.raw.MeteringDataService;

@Stateless
public class HourMeteringDataRawServiceImpl extends AbstractEntityService<HourMeteringDataRaw> implements MeteringDataService<HourMeteringDataRaw> {
   
	@Inject
    public HourMeteringDataRawServiceImpl(Repository<HourMeteringDataRaw> repository, Validator validator) {
        super(repository, validator);
    }

	public void saveAll(List<HourMeteringDataRaw> list) {
		HourMeteringDataRawRepository hourRepository = (HourMeteringDataRawRepository) repository;
		
    	list.stream().forEach(m -> {
    		HourMeteringDataRaw h = hourRepository.selectByEntity(m);
    		if (h==null) {
				m.setCreateDate(LocalDateTime.now());
				hourRepository.insert(m);
			}
    		else {
    			m.setId(h.getId());
    			m.setCreateDate(h.getCreateDate());
				m.setLastUpdateDate(LocalDateTime.now());
				hourRepository.update(m);
    		}
        });		
	}	
}
