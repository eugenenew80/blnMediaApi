package kz.kegoc.bln.repository.media.raw.impl;

import kz.kegoc.bln.entity.media.raw.HourMeteringDataRaw;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.raw.HourMeteringDataRawRepository;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

@Stateless
public class HourMeteringDataRawRepositoryImpl extends AbstractRepository<HourMeteringDataRaw> implements HourMeteringDataRawRepository {
	public HourMeteringDataRaw selectByEntity(HourMeteringDataRaw entity) {
		TypedQuery<HourMeteringDataRaw> typedQuery =  getEntityManager().createNamedQuery("HourMeteringDataRaw.findByEntity", HourMeteringDataRaw.class);
		
		typedQuery.setParameter("externalCode", entity.getExternalCode());
		typedQuery.setParameter("meteringDate", entity.getMeteringDate());
		typedQuery.setParameter("hour", entity.getHour());
		typedQuery.setParameter("unitCode", entity.getUnitCode());
		typedQuery.setParameter("dataSourceCode", entity.getDataSourceCode());
		typedQuery.setParameter("paramCode", entity.getParamCode());
		typedQuery.setParameter("wayEntering", entity.getWayEntering());
		typedQuery.setParameter("status", entity.getStatus());
		
		return typedQuery.getResultList().stream()
			.findFirst()
			.orElse(null);		
	}
}
