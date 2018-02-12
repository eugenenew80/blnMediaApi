package kz.kegoc.bln.service.data;

import kz.kegoc.bln.entity.data.EventLog;
import kz.kegoc.bln.service.common.EntityService;
import javax.ejb.Local;

@Local
public interface EventLogService extends EntityService<EventLog> {
}
