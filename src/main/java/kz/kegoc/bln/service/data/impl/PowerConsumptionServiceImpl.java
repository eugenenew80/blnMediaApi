package kz.kegoc.bln.service.data.impl;

import kz.kegoc.bln.entity.data.PowerConsumption;
import kz.kegoc.bln.repository.data.PowerConsumptionRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.data.PowerConsumptionService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class PowerConsumptionServiceImpl extends AbstractEntityService<PowerConsumption> implements PowerConsumptionService {

	@Inject
    public PowerConsumptionServiceImpl(PowerConsumptionRepository repository, Validator validator) {
        super(repository, validator);
        this.repository = repository;
    }

    public List<PowerConsumption> findByExternalCode(String externalCode, LocalDateTime meteringDateStart, LocalDateTime meteringDateEnd) {
        return repository.selectByExternalCode(externalCode, meteringDateStart, meteringDateEnd);
    }

    private PowerConsumptionRepository repository;
}
