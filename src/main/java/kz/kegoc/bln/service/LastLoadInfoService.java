package kz.kegoc.bln.service;

import kz.kegoc.bln.entity.media.LastLoadInfo;
import kz.kegoc.bln.common.service.EntityService;
import javax.ejb.Local;

@Local
public interface LastLoadInfoService extends EntityService<LastLoadInfo> {
	void atUpdateLastDate(Long batchId);
	void ptUpdateLastDate(Long batchId);
	void atLoad(Long batchId);
	void ptLoad(Long batchId);
}
