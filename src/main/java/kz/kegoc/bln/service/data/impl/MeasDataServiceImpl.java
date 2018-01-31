package kz.kegoc.bln.service.data.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import kz.kegoc.bln.entity.data.MeasData;
import kz.kegoc.bln.repository.data.MeteringDataRepository;
import kz.kegoc.bln.service.data.AbstractMeteringDataService;
import kz.kegoc.bln.service.data.MeteringDataService;

import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class MeasDataServiceImpl
    extends AbstractMeteringDataService<MeasData>
        implements MeteringDataService<MeasData> {
   
	@Inject
    public MeasDataServiceImpl(MeteringDataRepository<MeasData> repository, Validator validator) {
        super(repository, validator);
    }

	public MeasData findByEntity(MeasData entity) {
		return null;
	}

	public List<MeasData> findReadyData(Long meteringPointId, LocalDateTime meteringDate, String paramCode) {
		return null;
	}
}
