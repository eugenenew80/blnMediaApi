package kz.kegoc.bln.service.media.raw.impl;

import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import kz.kegoc.bln.entity.media.raw.DayMeteringDataRaw;
import kz.kegoc.bln.repository.media.raw.MeteringDataRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.raw.MeteringDataService;

@Stateless
public class DayMeteringDataRawServiceImpl extends AbstractEntityService<DayMeteringDataRaw> implements MeteringDataService<DayMeteringDataRaw> {
   
	@Inject
    public DayMeteringDataRawServiceImpl(MeteringDataRepository<DayMeteringDataRaw> repository, Validator validator) {
        super(repository, validator);
        this.meteringDataRepository=repository;
    }

	public void saveAll(List<DayMeteringDataRaw> list) {
    	list.stream().forEach(m -> {
    		DayMeteringDataRaw h = meteringDataRepository.selectByEntity(m);
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

	private MeteringDataRepository<DayMeteringDataRaw> meteringDataRepository;
}
