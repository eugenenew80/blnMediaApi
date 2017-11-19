package kz.kegoc.bln.repository.media.data;

import kz.kegoc.bln.entity.media.data.LastLoadInfo;
import kz.kegoc.bln.repository.common.Repository;

import javax.ejb.Local;

@Local
public interface LastLoadInfoRepository extends Repository<LastLoadInfo> {
	LastLoadInfo findByExternalCodeAndParamCode(String externalCode, String paramCode);
}
