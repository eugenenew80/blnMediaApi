package kz.kegoc.bln.service.data;

import kz.kegoc.bln.entity.common.MeteringValue;
import kz.kegoc.bln.repository.data.MeteringValueRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import javax.validation.Validator;
import java.util.List;

public abstract class AbstractMeteringValueService<T extends MeteringValue>
	extends AbstractEntityService<T>
		implements MeteringValueService<T> {

    public AbstractMeteringValueService(MeteringValueRepository<T> repository, Validator validator) {
        super(repository, validator);
        this.meteringDataRepository=repository;
    }

	public void saveAll(List<T> list) {
		meteringDataRepository.saveAll(list);
	}

	private MeteringValueRepository<T> meteringDataRepository;
}
