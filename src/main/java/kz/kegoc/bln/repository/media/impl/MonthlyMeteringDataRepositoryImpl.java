package kz.kegoc.bln.repository.media.impl;

import javax.ejb.Stateless;
import kz.kegoc.bln.entity.media.MonthlyMeteringData;
import kz.kegoc.bln.repository.common.AbstractMeteringDataRepository;
import kz.kegoc.bln.repository.media.MonthlyMeteringDataRepository;

@Stateless
public class MonthlyMeteringDataRepositoryImpl extends AbstractMeteringDataRepository<MonthlyMeteringData> implements MonthlyMeteringDataRepository {
}
