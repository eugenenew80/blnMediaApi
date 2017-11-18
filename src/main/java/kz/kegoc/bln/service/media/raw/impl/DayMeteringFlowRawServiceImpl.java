package kz.kegoc.bln.service.media.raw.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import kz.kegoc.bln.entity.media.raw.DayMeteringFlowRaw;
import kz.kegoc.bln.repository.media.raw.MeteringDataRawRepository;
import kz.kegoc.bln.service.media.raw.AbstractMeteringDataRawService;
import kz.kegoc.bln.service.media.raw.MeteringDataRawService;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class DayMeteringFlowRawServiceImpl
    extends AbstractMeteringDataRawService<DayMeteringFlowRaw>
        implements MeteringDataRawService<DayMeteringFlowRaw> {

	@Inject
    public DayMeteringFlowRawServiceImpl(MeteringDataRawRepository<DayMeteringFlowRaw> repository, Validator validator) {
        super(repository, validator);
    }

	public DayMeteringFlowRaw findByEntity(DayMeteringFlowRaw entity) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<DayMeteringFlowRaw> findReadyData(Long meteringPointId, LocalDateTime meteringDate, String paramCode) {
		return null;
	}
}
