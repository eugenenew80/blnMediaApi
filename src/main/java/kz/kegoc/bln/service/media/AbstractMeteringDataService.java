package kz.kegoc.bln.service.media;

import kz.kegoc.bln.entity.media.Metering;
import kz.kegoc.bln.repository.media.MeteringDataRawRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;

public abstract class AbstractMeteringDataService<T extends Metering>
	extends AbstractEntityService<T>
		implements MeteringDataService<T> {

    public AbstractMeteringDataService(MeteringDataRawRepository<T> repository, Validator validator) {
        super(repository, validator);
        this.meteringDataRepository=repository;
    }

	public void saveAll(List<T> list) {
    	list.stream().forEach(m -> {
    		Metering meteringData = meteringDataRepository.selectByEntity(m);
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

	private MeteringDataRawRepository<T> meteringDataRepository;
}
