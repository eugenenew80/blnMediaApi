package kz.kegoc.bln.service.data.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import kz.kegoc.bln.entity.data.MeteringReading;
import kz.kegoc.bln.repository.data.MeteringDataRepository;
import kz.kegoc.bln.service.data.AbstractMeteringDataService;
import kz.kegoc.bln.service.data.MeteringDataService;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class MeteringReadingServiceImpl
    extends AbstractMeteringDataService<MeteringReading>
        implements MeteringDataService<MeteringReading> {

	@Inject
    public MeteringReadingServiceImpl(MeteringDataRepository<MeteringReading> repository, Validator validator) {
        super(repository, validator);
        this.dayMeteringBalanceRawRepository = repository;
    }

	public MeteringReading findByEntity(MeteringReading entity) {
		return dayMeteringBalanceRawRepository.selectByEntity(entity);
	}

	public List<MeteringReading> findReadyData(Long meteringPointId, LocalDateTime meteringDate, String paramCode) {
		return dayMeteringBalanceRawRepository.selectReadyData(meteringPointId, meteringDate, paramCode);
	}

	private MeteringDataRepository<MeteringReading> dayMeteringBalanceRawRepository;
}
