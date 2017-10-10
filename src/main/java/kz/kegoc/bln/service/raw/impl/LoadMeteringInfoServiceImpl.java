package kz.kegoc.bln.service.raw.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.entity.media.HourlyMeteringDataRaw;
import kz.kegoc.bln.entity.media.LoadMeteringInfo;
import kz.kegoc.bln.repository.common.Repository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.raw.LoadMeteringInfoService;


@Stateless
public class LoadMeteringInfoServiceImpl extends AbstractEntityService<LoadMeteringInfo> implements LoadMeteringInfoService {
    
	@Inject
    public LoadMeteringInfoServiceImpl(Repository<LoadMeteringInfo> repository, Validator validator) {
        super(repository, validator);
    }

	public void updateLoadMeteringInfo(List<HourlyMeteringDataRaw> meteringData, LocalDateTime endDateTime) {
		meteringData.stream()
		.map(t -> t.getExternalCode())
		.distinct()
		.collect(Collectors.toList())
		.forEach(externalCode -> {

			LocalDateTime lastLoadedDate = meteringData.stream()
				.filter(p -> p.getExternalCode().equals(externalCode))	
				.map(p -> p.getMeteringDate())
				.max(LocalDateTime::compareTo).get();

			MeteringPoint p  = em.createNamedQuery("MeteringPoint.findByExternalCode", MeteringPoint.class)
				.setParameter("externalCode", externalCode)
				.getResultList()
				.stream()
				.findFirst()
				.orElse(null);

			if (p!=null ) {
				LoadMeteringInfo l = em.find(LoadMeteringInfo.class, p.getId());
				if (l==null) 
					l = new LoadMeteringInfo();
				
				l.setId(p.getId());
				l.setLastLoadedDate(lastLoadedDate);
				l.setLastRequestedDate(endDateTime);
				
				em.persist(l);
			}
		});			
	}	
}
