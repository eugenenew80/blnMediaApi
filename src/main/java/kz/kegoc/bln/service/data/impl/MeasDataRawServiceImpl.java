package kz.kegoc.bln.service.data.impl;

import kz.kegoc.bln.entity.data.MeasDataRaw;
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
    extends AbstractMeteringDataService<MeasDataRaw>
        implements MeteringDataService<MeasDataRaw> {

	@Inject
    public MeasDataRawServiceImpl(MeteringDataRepository<MeasDataRaw> repository, Validator validator) {
        super(repository, validator);
    }

	public MeasDataRaw findByEntity(MeasDataRaw entity) {
		return null;
	}

	public List<MeasDataRaw> findReadyData(Long meteringPointId, LocalDateTime meteringDate, String paramCode) {
		return null;
	}
}
