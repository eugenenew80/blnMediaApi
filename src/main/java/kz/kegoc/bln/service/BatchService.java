package kz.kegoc.bln.service;

import kz.kegoc.bln.entity.media.Batch;
import kz.kegoc.bln.common.service.EntityService;

import javax.ejb.Local;

@Local
public interface BatchService extends EntityService<Batch> {
}
