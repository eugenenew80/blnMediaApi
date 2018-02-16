package kz.kegoc.bln.repository.data;

import kz.kegoc.bln.entity.data.EventLog;
import kz.kegoc.bln.repository.common.Repository;
import javax.ejb.Local;

@Local
public interface EventLogRepository extends Repository<EventLog> { }
