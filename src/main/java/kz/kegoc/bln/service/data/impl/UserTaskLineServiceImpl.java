package kz.kegoc.bln.service.data.impl;

import kz.kegoc.bln.entity.data.UserTaskLine;
import kz.kegoc.bln.repository.data.UserTaskLineRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.data.UserTaskLineService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;


@Stateless
public class UserTaskLineServiceImpl extends AbstractEntityService<UserTaskLine> implements UserTaskLineService {

	@Inject
    public UserTaskLineServiceImpl(UserTaskLineRepository repository, Validator validator) {
        super(repository, validator);
        this.repository = repository;
    }

	private UserTaskLineRepository repository;
}
