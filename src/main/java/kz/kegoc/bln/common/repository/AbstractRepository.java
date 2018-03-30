package kz.kegoc.bln.common.repository;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import kz.kegoc.bln.common.interfaces.HasId;

public abstract class AbstractRepository<T extends HasId> implements Repository<T> {

	public List<T> selectAll() {
		TypedQuery<T> query = em.createNamedQuery(clazz.getSimpleName() +  ".findAll", clazz);
		return query.getResultList();
	}

	public T selectById(Long entityId) {
		em.getEntityManagerFactory().getCache().evict(clazz, entityId);
		return em.find(clazz, entityId);
	}

	public T insert(T entity) {
		em.persist(entity);
		return entity;
	}

	public T update(T entity) {
		em.merge(entity);		
		return entity;
	}

	public boolean delete(Long entityId) {
		T entity = selectById(entityId);
		if (entity!=null) {
			em.remove(entity);
			return true;
		}
		return false;
	}

	
	public EntityManager getEntityManager() {
		return em;
	}

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}	
		
	protected void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}	

	
	@PersistenceContext(unitName = "bln")
	private EntityManager em;
	private Class<T> clazz;
}
