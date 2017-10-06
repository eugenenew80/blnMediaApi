package kz.kegoc.bln.repository.raw.impl;

import javax.ejb.Stateless;
import kz.kegoc.bln.entity.media.DailyMeteringDataRaw;
import kz.kegoc.bln.repository.common.AbstractMeteringDataRepository;
import kz.kegoc.bln.repository.raw.DailyMeteringDataRawRepository;

@Stateless
public class DailyMeteringDataRawRepositoryImpl extends AbstractMeteringDataRepository<DailyMeteringDataRaw> implements DailyMeteringDataRawRepository {
}
