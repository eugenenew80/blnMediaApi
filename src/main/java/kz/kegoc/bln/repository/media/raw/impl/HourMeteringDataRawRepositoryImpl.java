package kz.kegoc.bln.repository.media.raw.impl;

import kz.kegoc.bln.entity.media.raw.HourMeteringDataRaw;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.raw.HourMeteringDataRawRepository;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class HourMeteringDataRawRepositoryImpl extends AbstractRepository<HourMeteringDataRaw> implements HourMeteringDataRawRepository {
    public void insertAll(List<HourMeteringDataRaw> list) {
        list.stream().forEach(this::insert);
    }
}
