package kz.kegoc.bln.service.media.raw.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import kz.kegoc.bln.entity.media.raw.HourMeteringFlowRaw;
import kz.kegoc.bln.repository.media.raw.MeteringDataRawRepository;
import kz.kegoc.bln.service.media.raw.AbstractMeteringDataRawService;
import kz.kegoc.bln.service.media.raw.MeteringDataRawService;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class HourMeteringFlowRawServiceImpl
    extends AbstractMeteringDataRawService<HourMeteringFlowRaw>
        implements MeteringDataRawService<HourMeteringFlowRaw> {
   
	@Inject
    public HourMeteringFlowRawServiceImpl(MeteringDataRawRepository<HourMeteringFlowRaw> repository, Validator validator) {
        super(repository, validator);
    }

	public HourMeteringFlowRaw findByEntity(HourMeteringFlowRaw entity) {
		return null;
	}

	public List<HourMeteringFlowRaw> findReadyData(Long meteringPointId, LocalDateTime meteringDate, String paramCode) {
		return null;
	}
}
