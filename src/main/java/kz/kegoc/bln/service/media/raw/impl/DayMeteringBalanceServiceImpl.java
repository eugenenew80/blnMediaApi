package kz.kegoc.bln.service.media.raw.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import kz.kegoc.bln.entity.media.raw.DayMeteringBalance;
import kz.kegoc.bln.repository.media.raw.MeteringDataRepository;
import kz.kegoc.bln.service.media.raw.AbstractMeteringDataService;
import kz.kegoc.bln.service.media.raw.MeteringDataService;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class DayMeteringBalanceServiceImpl
    extends AbstractMeteringDataService<DayMeteringBalance>
        implements MeteringDataService<DayMeteringBalance> {

	@Inject
    public DayMeteringBalanceServiceImpl(MeteringDataRepository<DayMeteringBalance> repository, Validator validator) {
        super(repository, validator);
        this.dayMeteringBalanceRawRepository = repository;
    }

	public DayMeteringBalance findByEntity(DayMeteringBalance entity) {
		return dayMeteringBalanceRawRepository.selectByEntity(entity);
	}

	public List<DayMeteringBalance> findReadyData(Long meteringPointId, LocalDateTime meteringDate, String paramCode) {
		return dayMeteringBalanceRawRepository.selectReadyData(meteringPointId, meteringDate, paramCode);
	}

	private MeteringDataRepository<DayMeteringBalance> dayMeteringBalanceRawRepository;
}
