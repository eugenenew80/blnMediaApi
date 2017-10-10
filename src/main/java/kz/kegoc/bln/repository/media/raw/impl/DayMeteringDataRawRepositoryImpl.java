package kz.kegoc.bln.repository.media.raw.impl;

import javax.ejb.Stateless;
import kz.kegoc.bln.entity.media.raw.DayMeteringDataRaw;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.raw.DayMeteringDataRawRepository;

import java.util.List;

@Stateless
public class DayMeteringDataRawRepositoryImpl extends AbstractRepository<DayMeteringDataRaw> implements DayMeteringDataRawRepository {
    public void insertAll(List<DayMeteringDataRaw> list) {
        list.stream().forEach(this::insert);
    }
}
