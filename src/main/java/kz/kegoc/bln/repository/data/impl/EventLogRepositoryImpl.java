package kz.kegoc.bln.repository.data.impl;

import kz.kegoc.bln.entity.data.EventLog;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.data.EventLogRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
public class EventLogRepositoryImpl extends AbstractRepository<EventLog> implements EventLogRepository {
	public EventLogRepositoryImpl() { setClazz(EventLog.class); }

	public EventLogRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
