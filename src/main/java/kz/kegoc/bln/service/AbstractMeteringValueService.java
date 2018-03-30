package kz.kegoc.bln.service;

import kz.kegoc.bln.common.interfaces.MeteringValue;
import kz.kegoc.bln.repository.MeteringValueRepository;
import kz.kegoc.bln.common.service.AbstractEntityService;
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
