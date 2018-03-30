package kz.kegoc.bln.repository;

import kz.kegoc.bln.entity.media.LastLoadInfo;
import kz.kegoc.bln.common.repository.Repository;
import javax.ejb.Local;

@Local
public interface LastLoadInfoRepository extends Repository<LastLoadInfo> {
    void atUpdateLastDate(Long batchId);
    void ptUpdateLastDate(Long batchId);
    void atLoad(Long batchId);
    void ptLoad(Long batchId);
}
