package kz.kegoc.bln.service.data.impl;

import kz.kegoc.bln.entity.data.UserTaskHeader;
import kz.kegoc.bln.repository.data.UserTaskHeaderRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.data.UserTaskHeaderService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;


@Stateless
public class UserTaskHeaderServiceImpl extends AbstractEntityService<UserTaskHeader> implements UserTaskHeaderService {

	@Inject
    public UserTaskHeaderServiceImpl(UserTaskHeaderRepository repository, Validator validator) {
        super(repository, validator);
        this.repository = repository;
    }

	private UserTaskHeaderRepository repository;
}
