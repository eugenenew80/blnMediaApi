package kz.kegoc.bln.repository.data;

import kz.kegoc.bln.entity.data.Parameter;
import kz.kegoc.bln.repository.common.Repository;
import javax.ejb.Local;

@Local
public interface ParameterRepository extends Repository<Parameter> { }
