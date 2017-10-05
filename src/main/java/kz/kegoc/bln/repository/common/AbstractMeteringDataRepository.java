package kz.kegoc.bln.repository.common;

import kz.kegoc.bln.entity.common.HasId;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public abstract class AbstractMeteringDataRepository <T extends HasId> implements MeteringDataRepository<T> {

    public T insert(T entity) {
        em.persist(entity);
        return entity;
    }

    public void insertAll(List<T> list) {
        list.stream().forEach(this::insert);
    }

    @PersistenceContext(unitName = "bln")
    private EntityManager em;
}
