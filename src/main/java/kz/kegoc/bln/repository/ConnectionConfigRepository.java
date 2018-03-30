package kz.kegoc.bln.repository;

import kz.kegoc.bln.entity.media.ConnectionConfig;
import kz.kegoc.bln.common.repository.Repository;
import javax.ejb.Local;

@Local
public interface ConnectionConfigRepository extends Repository<ConnectionConfig> { }
