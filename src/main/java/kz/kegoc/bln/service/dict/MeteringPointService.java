package kz.kegoc.bln.service.dict;

import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.service.common.EntityService;

import javax.ejb.Local;

@Local
public interface MeteringPointService extends EntityService<MeteringPoint> {
    MeteringPoint findByExternalCode(String externalCode);
}
