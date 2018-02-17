package kz.kegoc.bln.service.data.impl;

import kz.kegoc.bln.entity.data.PowerConsumptionRaw;
import kz.kegoc.bln.repository.data.MeteringDataRepository;
import kz.kegoc.bln.service.data.AbstractMeteringDataService;
import kz.kegoc.bln.service.data.MeteringDataService;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.util.List;

@Stateless
public class PowerConsumptionRawServiceImpl
    extends AbstractMeteringDataService<PowerConsumptionRaw>
        implements MeteringDataService<PowerConsumptionRaw> {

	@Inject
    public PowerConsumptionRawServiceImpl(MeteringDataRepository<PowerConsumptionRaw> repository, Validator validator) {
        super(repository, validator);
    }

    @Override
    public void saveAll(List<PowerConsumptionRaw> list) {
        super.saveAll(list);
    }
}
