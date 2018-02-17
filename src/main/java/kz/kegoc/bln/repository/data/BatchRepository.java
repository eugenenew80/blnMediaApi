package kz.kegoc.bln.repository.data;

import kz.kegoc.bln.entity.data.Batch;
import kz.kegoc.bln.repository.common.Repository;

import javax.ejb.Local;

@Local
public interface BatchRepository extends Repository<Batch> { }
