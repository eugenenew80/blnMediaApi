package kz.kegoc.bln.repository.media.impl;

import javax.ejb.Stateless;
import kz.kegoc.bln.entity.media.DailyMeteringData;
import kz.kegoc.bln.repository.common.AbstractMeteringDataRepository;
import kz.kegoc.bln.repository.media.DailyMeteringDataRepository;

@Stateless
public class DailyMeteringDataRepositoryImpl extends AbstractMeteringDataRepository<DailyMeteringData> implements DailyMeteringDataRepository {
}
