package kz.kegoc.bln.service.data.impl;

import kz.kegoc.bln.entity.data.EventLog;
import kz.kegoc.bln.repository.data.EventLogRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.data.EventLogService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;


@Stateless
public class EventLogServiceImpl extends AbstractEntityService<EventLog> implements EventLogService {

	@Inject
    public EventLogServiceImpl(EventLogRepository repository, Validator validator) {
        super(repository, validator);
        this.repository = repository;
    }

	private EventLogRepository repository;
}
