package kz.kegoc.bln.repository.media.raw.impl;

import javax.ejb.Stateless;
import kz.kegoc.bln.entity.media.raw.MonthMeteringDataRaw;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.raw.MonthMeteringDataRawRepository;

import java.util.List;

@Stateless
public class MonthMeteringDataRawRepositoryImpl extends AbstractRepository<MonthMeteringDataRaw> implements MonthMeteringDataRawRepository {
    public void insertAll(List<MonthMeteringDataRaw> list) {
        list.stream().forEach(this::insert);
    }
}
