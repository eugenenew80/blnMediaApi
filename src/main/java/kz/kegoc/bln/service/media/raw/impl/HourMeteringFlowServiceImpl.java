package kz.kegoc.bln.service.media.raw.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import kz.kegoc.bln.entity.media.raw.HourMeteringFlow;
import kz.kegoc.bln.repository.media.raw.MeteringDataRepository;
import kz.kegoc.bln.service.media.raw.AbstractMeteringDataService;
import kz.kegoc.bln.service.media.raw.MeteringDataService;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class HourMeteringFlowServiceImpl
    extends AbstractMeteringDataService<HourMeteringFlow>
        implements MeteringDataService<HourMeteringFlow> {
   
	@Inject
    public HourMeteringFlowServiceImpl(MeteringDataRepository<HourMeteringFlow> repository, Validator validator) {
        super(repository, validator);
    }

	public HourMeteringFlow findByEntity(HourMeteringFlow entity) {
		return null;
	}

	public List<HourMeteringFlow> findReadyData(Long meteringPointId, LocalDateTime meteringDate, String paramCode) {
		return null;
	}
}
