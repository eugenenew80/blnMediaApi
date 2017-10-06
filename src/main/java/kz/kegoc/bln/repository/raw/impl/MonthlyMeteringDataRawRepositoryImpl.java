package kz.kegoc.bln.repository.raw.impl;

import javax.ejb.Stateless;
import kz.kegoc.bln.entity.media.MonthlyMeteringDataRaw;
import kz.kegoc.bln.repository.common.AbstractMeteringDataRepository;
import kz.kegoc.bln.repository.raw.MonthlyMeteringDataRawRepository;

@Stateless
public class MonthlyMeteringDataRawRepositoryImpl extends AbstractMeteringDataRepository<MonthlyMeteringDataRaw> implements MonthlyMeteringDataRawRepository {
}
