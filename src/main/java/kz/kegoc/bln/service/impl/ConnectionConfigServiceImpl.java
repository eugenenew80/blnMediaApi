package kz.kegoc.bln.service.impl;

import kz.kegoc.bln.entity.media.ConnectionConfig;
import kz.kegoc.bln.repository.ConnectionConfigRepository;
import kz.kegoc.bln.common.service.AbstractEntityService;
import kz.kegoc.bln.service.ConnectionConfigService;
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
