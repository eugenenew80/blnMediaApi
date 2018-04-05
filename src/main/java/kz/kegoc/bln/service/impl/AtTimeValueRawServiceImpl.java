package kz.kegoc.bln.service.impl;

import kz.kegoc.bln.common.service.AbstractEntityService;
import kz.kegoc.bln.imp.raw.AtTimeValueRaw;
import kz.kegoc.bln.repository.AtTimeValueRawRepository;
import kz.kegoc.bln.service.AtTimeValueRawService;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.util.List;

@Stateless
public class AtTimeValueRawServiceImpl
    extends AbstractEntityService<AtTimeValueRaw>
        implements AtTimeValueRawService {

	@Inject
    public AtTimeValueRawServiceImpl(AtTimeValueRawRepository repository, Validator validator) {
        super(repository, validator);
        this.atTimeValueRawRepository = repository;
    }

    @Override
    public void saveAll(List<AtTimeValueRaw> list) {
        atTimeValueRawRepository.saveAll(list);
    }

    AtTimeValueRawRepository atTimeValueRawRepository;
}
