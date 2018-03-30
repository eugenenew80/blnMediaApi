package kz.kegoc.bln.service;

import kz.kegoc.bln.entity.media.ConnectionConfig;
import kz.kegoc.bln.common.service.EntityService;
import javax.ejb.Local;

@Local
public interface ConnectionConfigService extends EntityService<ConnectionConfig> {
}
