package kz.kegoc.bln.service.data.impl;

import kz.kegoc.bln.entity.data.EmcosConfig;
import kz.kegoc.bln.repository.data.EmcosConfigRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.data.EmcosConfigService;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;


@Stateless
public class EmcosConfigServiceImpl extends AbstractEntityService<EmcosConfig> implements EmcosConfigService {

	@Inject
    public EmcosConfigServiceImpl(EmcosConfigRepository repository, Validator validator) {
        super(repository, validator);
        this.repository = repository;
    }

	private EmcosConfigRepository repository;
}
