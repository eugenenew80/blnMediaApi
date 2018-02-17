package kz.kegoc.bln.service.data;

import kz.kegoc.bln.entity.data.LastLoadInfo;
import kz.kegoc.bln.service.common.EntityService;
import javax.ejb.Local;

@Local
public interface LastLoadInfoService extends EntityService<LastLoadInfo> {
	void mrUpdateLastDate(Long batchId);
	void pcUpdateLastDate(Long batchId);
	void mrLoad(Long batchId);
	void pcLoad(Long batchId);
}
