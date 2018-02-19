package kz.kegoc.bln.repository.data;

import kz.kegoc.bln.entity.data.ConnectionConfig;
import kz.kegoc.bln.repository.common.Repository;
import javax.ejb.Local;

@Local
public interface ConnectionConfigRepository extends Repository<ConnectionConfig> { }
