package kz.kegoc.bln.repository.dict;

import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.repository.common.Repository;

public interface MeteringPointRepository extends Repository<MeteringPoint> {
	MeteringPoint selectByCode(String externalCode);
}
