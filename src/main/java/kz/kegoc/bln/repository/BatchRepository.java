package kz.kegoc.bln.repository;

import kz.kegoc.bln.entity.media.Batch;
import kz.kegoc.bln.common.repository.Repository;

import javax.ejb.Local;

@Local
public interface BatchRepository extends Repository<Batch> { }
