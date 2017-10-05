package kz.kegoc.bln.repository.media.impl;

import kz.kegoc.bln.entity.media.HourlyMeteringData;
import kz.kegoc.bln.repository.common.AbstractMeteringDataRepository;
import kz.kegoc.bln.repository.media.HourlyMeteringDataRepository;
import javax.ejb.Stateless;


@Stateless
public class HourlyMeteringDataRepositoryImpl extends AbstractMeteringDataRepository<HourlyMeteringData> implements HourlyMeteringDataRepository {

}
