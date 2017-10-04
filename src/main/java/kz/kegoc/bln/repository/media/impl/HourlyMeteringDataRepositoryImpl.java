package kz.kegoc.bln.repository.media.impl;

import kz.kegoc.bln.entity.media.HourlyMeteringData;
import kz.kegoc.bln.repository.media.HourlyMeteringDataRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class HourlyMeteringDataRepositoryImpl implements HourlyMeteringDataRepository {

	public HourlyMeteringData insert(HourlyMeteringData entity) {
		em.persist(entity);
		return entity;
	}

	
	public void insertAll(List<HourlyMeteringData> list) {
		list.stream().forEach(this::insert);
	}
	
	
	@PersistenceContext(unitName = "bln")
	private EntityManager em;
}
