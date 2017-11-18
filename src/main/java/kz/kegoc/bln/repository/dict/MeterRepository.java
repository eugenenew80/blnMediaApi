package kz.kegoc.bln.repository.dict;

import kz.kegoc.bln.entity.dict.Meter;
import kz.kegoc.bln.repository.common.Repository;

import javax.ejb.Local;

@Local
public interface MeterRepository extends Repository<Meter> { }
