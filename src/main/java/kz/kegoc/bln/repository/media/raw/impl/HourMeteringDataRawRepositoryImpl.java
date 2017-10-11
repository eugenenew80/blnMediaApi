package kz.kegoc.bln.repository.media.raw.impl;

import kz.kegoc.bln.entity.media.raw.HourMeteringDataRaw;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.raw.HourMeteringDataRawRepository;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import java.util.List;

@Stateless
public class HourMeteringDataRawRepositoryImpl extends AbstractRepository<HourMeteringDataRaw> implements HourMeteringDataRawRepository {
    public void insertAll(List<HourMeteringDataRaw> list) {
		String queryStr = "select m from HourMeteringDataRaw m where m.externalCode=:externalCode "
								+ "and m.meteringDate=:meteringDate "
								+ "and m.hour=:hour "
								+ "and m.dataSourceCode=:dataSourceCode "
								+ "and m.wayEntering=:wayEntering "
								+ "and m.status=:status "
								+ "and m.unitCode=:unitCode "
								+ "and m.paramCode=:paramCode";
		
		TypedQuery<HourMeteringDataRaw> typedQuery = getEntityManager().createQuery(queryStr.trim(), HourMeteringDataRaw.class);
    	
    	list.stream().forEach(m -> {
    		typedQuery.setParameter("externalCode", m.getExternalCode());
    		typedQuery.setParameter("meteringDate", m.getMeteringDate());
    		typedQuery.setParameter("hour", m.getHour());
    		typedQuery.setParameter("unitCode", m.getUnitCode());
    		typedQuery.setParameter("dataSourceCode", m.getDataSourceCode());
    		typedQuery.setParameter("paramCode", m.getParamCode());
    		typedQuery.setParameter("wayEntering", m.getWayEntering());
    		typedQuery.setParameter("status", m.getStatus());
    		
    		HourMeteringDataRaw h = typedQuery.getResultList().stream()
				.findFirst()
				.orElse(null);
    		
    		if (h==null) {
    			insert(m);
    		}
    		else {
    			m.setId(h.getId());
    			update(m);
    		}
        });
    }
}
