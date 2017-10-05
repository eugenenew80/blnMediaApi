package kz.kegoc.bln.repository.media.impl;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kz.kegoc.bln.entity.media.MonthlyMeteringData;
import kz.kegoc.bln.repository.media.MonthlyMeteringDataRepository;

@Stateless
public class MonthlyMeteringDataRepositoryImpl implements MonthlyMeteringDataRepository {

	public MonthlyMeteringData insert(MonthlyMeteringData entity) {
		em.persist(entity);
		return entity;
	}
	
	public void insertAll(List<MonthlyMeteringData> list) {
		list.stream().forEach(this::insert);
	}	
	
	@PersistenceContext(unitName = "bln")
	private EntityManager em;
}
