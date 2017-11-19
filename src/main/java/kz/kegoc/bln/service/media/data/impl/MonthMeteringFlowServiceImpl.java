package kz.kegoc.bln.service.media.data.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import kz.kegoc.bln.entity.media.data.MonthMeteringFlow;
import kz.kegoc.bln.repository.media.data.MeteringDataRepository;
import kz.kegoc.bln.service.media.data.AbstractMeteringDataService;
import kz.kegoc.bln.service.media.data.MeteringDataService;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class MonthMeteringFlowServiceImpl
	extends AbstractMeteringDataService<MonthMeteringFlow>
		implements MeteringDataService<MonthMeteringFlow> {
   
	@Inject
    public MonthMeteringFlowServiceImpl(MeteringDataRepository<MonthMeteringFlow> repository, Validator validator) {
		super(repository, validator);
	}

	public MonthMeteringFlow findByEntity(MonthMeteringFlow entity) {
		return null;
	}

	public List<MonthMeteringFlow> findReadyData(Long meteringPointId, LocalDateTime meteringDate, String paramCode) {
		return null;
	}
}
