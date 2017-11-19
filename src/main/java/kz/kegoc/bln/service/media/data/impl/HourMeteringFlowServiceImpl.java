package kz.kegoc.bln.service.media.data.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import kz.kegoc.bln.entity.media.data.HourMeteringFlow;
import kz.kegoc.bln.repository.media.data.MeteringDataRepository;
import kz.kegoc.bln.service.media.data.AbstractMeteringDataService;
import kz.kegoc.bln.service.media.data.MeteringDataService;

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
