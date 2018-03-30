package kz.kegoc.bln.repository;

import kz.kegoc.bln.entity.media.Parameter;
import kz.kegoc.bln.common.repository.Repository;
import javax.ejb.Local;

@Local
public interface ParameterRepository extends Repository<Parameter> { }
