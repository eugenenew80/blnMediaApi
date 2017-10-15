package kz.kegoc.bln.service.media;

import kz.kegoc.bln.entity.media.MeteringData;
import kz.kegoc.bln.repository.media.raw.MeteringDataRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;

import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;

public abstract class AbstractMeteringDataService<T extends MeteringData>
	extends AbstractEntityService<T>
		implements MeteringDataService<T> {

    public AbstractMeteringDataService(MeteringDataRepository<T> repository, Validator validator) {
        super(repository, validator);
        this.meteringDataRepository=repository;
    }

	public void saveAll(List<T> list) {
    	list.stream().forEach(m -> {
    		MeteringData meteringData = meteringDataRepository.selectByEntity(m);
			if (meteringData==null) {
				m.setCreateDate(LocalDateTime.now());
				meteringDataRepository.insert(m);
			}
			else {
				m.setId(meteringData.getId());
				m.setCreateDate(meteringData.getCreateDate());
				m.setLastUpdateDate(LocalDateTime.now());
				meteringDataRepository.update(m);
			}
        });		
	}

	private MeteringDataRepository<T> meteringDataRepository;
}
