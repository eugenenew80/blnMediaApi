package kz.kegoc.bln.service.data;

import kz.kegoc.bln.entity.data.ConnectionConfig;
import kz.kegoc.bln.service.common.EntityService;
import javax.ejb.Local;

@Local
public interface ConnectionConfigService extends EntityService<ConnectionConfig> {
}
