package kz.kegoc.bln.service.data.impl;

import kz.kegoc.bln.entity.data.PowerConsumptionRaw;
import kz.kegoc.bln.repository.data.MeteringDataRepository;
import kz.kegoc.bln.service.data.AbstractMeteringDataService;
import kz.kegoc.bln.service.data.MeteringDataService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class MeasDataRawServiceImpl
    extends AbstractMeteringDataService<PowerConsumptionRaw>
        implements MeteringDataService<PowerConsumptionRaw> {

	@Inject
    public MeasDataRawServiceImpl(MeteringDataRepository<PowerConsumptionRaw> repository, Validator validator) {
        super(repository, validator);
    }

	public PowerConsumptionRaw findByEntity(PowerConsumptionRaw entity) {
		return null;
	}

	public List<PowerConsumptionRaw> findReadyData(Long meteringPointId, LocalDateTime meteringDate, String paramCode) {
		return null;
	}
}
