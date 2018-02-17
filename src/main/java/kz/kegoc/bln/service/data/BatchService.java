package kz.kegoc.bln.service.data;

import kz.kegoc.bln.entity.data.Batch;
import kz.kegoc.bln.service.common.EntityService;

import javax.ejb.Local;

@Local
public interface BatchService extends EntityService<Batch> {
}
