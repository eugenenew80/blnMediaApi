package kz.kegoc.bln.service.media.raw.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import kz.kegoc.bln.entity.media.raw.DayMeteringFlow;
import kz.kegoc.bln.repository.media.raw.MeteringDataRepository;
import kz.kegoc.bln.service.media.raw.AbstractMeteringDataService;
import kz.kegoc.bln.service.media.raw.MeteringDataService;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class DayMeteringFlowServiceImpl
    extends AbstractMeteringDataService<DayMeteringFlow>
        implements MeteringDataService<DayMeteringFlow> {

	@Inject
    public DayMeteringFlowServiceImpl(MeteringDataRepository<DayMeteringFlow> repository, Validator validator) {
        super(repository, validator);
    }

	public DayMeteringFlow findByEntity(DayMeteringFlow entity) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<DayMeteringFlow> findReadyData(Long meteringPointId, LocalDateTime meteringDate, String paramCode) {
		return null;
	}
}
