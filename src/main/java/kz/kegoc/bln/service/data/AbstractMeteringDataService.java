package kz.kegoc.bln.service.data;

import kz.kegoc.bln.entity.common.Metering;
import kz.kegoc.bln.repository.data.MeteringDataRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;

public abstract class AbstractMeteringDataService<T extends Metering>
	extends AbstractEntityService<T>
		implements MeteringDataService<T> {

    public AbstractMeteringDataService(MeteringDataRepository<T> repository, Validator validator) {
        super(repository, validator);
        this.meteringDataRepository=repository;
    }

	public void saveAll(List<T> list) {
		meteringDataRepository.saveAll(list);
	}

	private MeteringDataRepository<T> meteringDataRepository;
}
