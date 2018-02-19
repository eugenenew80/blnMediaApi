package kz.kegoc.bln.service.data.impl;

import kz.kegoc.bln.entity.data.ConnectionConfig;
import kz.kegoc.bln.repository.data.ConnectionConfigRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.data.ConnectionConfigService;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;


@Stateless
public class ConnectionConfigServiceImpl extends AbstractEntityService<ConnectionConfig> implements ConnectionConfigService {

	@Inject
    public ConnectionConfigServiceImpl(ConnectionConfigRepository repository, Validator validator) {
        super(repository, validator);
        this.repository = repository;
    }

	private ConnectionConfigRepository repository;
}
