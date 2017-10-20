package kz.kegoc.bln.repository.media;

import kz.kegoc.bln.entity.media.LastLoadInfo;
import kz.kegoc.bln.repository.common.Repository;

public interface LastLoadInfoRepository extends Repository<LastLoadInfo> {
	LastLoadInfo findByExternalCodeAndParamCode(String externalCode, String paramCode);
}
