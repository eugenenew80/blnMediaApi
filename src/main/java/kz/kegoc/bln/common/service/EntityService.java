package kz.kegoc.bln.common.service;

import java.util.List;
import kz.kegoc.bln.common.interfaces.HasId;

public interface EntityService<T extends HasId> {
	List<T> findAll();
	
	T findById(Long entityId);

	T create(T entity);

	T update(T entity);

    boolean delete(Long entityId);
}
