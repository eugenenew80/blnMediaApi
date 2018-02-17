package kz.kegoc.bln.repository.data;

import kz.kegoc.bln.entity.data.LastLoadInfo;
import kz.kegoc.bln.repository.common.Repository;

import javax.ejb.Local;

@Local
public interface LastLoadInfoRepository extends Repository<LastLoadInfo> {
    void mrUpdateLastDate(Long batchId);
    void pcUpdateLastDate(Long batchId);
    void mrLoad(Long batchId);
    void pcLoad(Long batchId);
}
