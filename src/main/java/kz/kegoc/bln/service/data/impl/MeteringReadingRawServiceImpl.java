package kz.kegoc.bln.service.data.impl;

import kz.kegoc.bln.entity.data.MeteringReadingRaw;
import kz.kegoc.bln.repository.data.MeteringDataRepository;
import kz.kegoc.bln.service.data.AbstractMeteringDataService;
import kz.kegoc.bln.service.data.MeteringDataService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class MeteringReadingRawServiceImpl
    extends AbstractMeteringDataService<MeteringReadingRaw>
        implements MeteringDataService<MeteringReadingRaw> {

	@Inject
    public MeteringReadingRawServiceImpl(MeteringDataRepository<MeteringReadingRaw> repository, Validator validator) {
        super(repository, validator);
    }

	public MeteringReadingRaw findByEntity(MeteringReadingRaw entity) {
		return null;
	}

	public List<MeteringReadingRaw> findReadyData(Long meteringPointId, LocalDateTime meteringDate, String paramCode) {
		return null;
	}
}
