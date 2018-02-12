package kz.kegoc.bln.repository.data;

import kz.kegoc.bln.entity.data.LastLoadInfo;
import kz.kegoc.bln.repository.common.Repository;

import javax.ejb.Local;

@Local
public interface LastLoadInfoRepository extends Repository<LastLoadInfo> {
	LastLoadInfo findByExternalCodeAndParamCode(String externalCode, String paramCode);
}