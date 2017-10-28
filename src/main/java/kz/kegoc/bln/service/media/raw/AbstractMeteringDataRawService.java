package kz.kegoc.bln.service.media.raw;

import kz.kegoc.bln.entity.media.Metering;
import kz.kegoc.bln.repository.media.raw.MeteringDataRawRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;

public abstract class AbstractMeteringDataRawService<T extends Metering>
	extends AbstractEntityService<T>
		implements MeteringDataRawService<T> {

    public AbstractMeteringDataRawService(MeteringDataRawRepository<T> repository, Validator validator) {
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
