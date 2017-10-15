package kz.kegoc.bln.service.media.raw.impl;

import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import kz.kegoc.bln.entity.media.MeteringData;
import kz.kegoc.bln.entity.media.raw.HourMeteringDataRaw;
import kz.kegoc.bln.repository.media.raw.MeteringDataRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.raw.MeteringDataService;

@Stateless
public class HourMeteringDataRawServiceImpl extends AbstractEntityService<HourMeteringDataRaw> implements MeteringDataService<HourMeteringDataRaw> {
   
	@Inject
    public HourMeteringDataRawServiceImpl(MeteringDataRepository<HourMeteringDataRaw> repository, Validator validator) {
        super(repository, validator);
        this.meteringDataRepository=repository;
    }

	public void saveAll(List<HourMeteringDataRaw> list) {
    	list.stream().forEach(m -> {
    		MeteringData h = meteringDataRepository.selectByEntity(m);
    		if (h==null) {
				m.setCreateDate(LocalDateTime.now());
				meteringDataRepository.insert(m);
			}
    		else {
    			m.setId(h.getId());
    			m.setCreateDate(h.getCreateDate());
				m.setLastUpdateDate(LocalDateTime.now());
				meteringDataRepository.update(m);
    		}
        });		
	}

	private MeteringDataRepository<HourMeteringDataRaw> meteringDataRepository;
}
