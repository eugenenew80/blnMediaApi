package kz.kegoc.bln.common.repository;

import java.util.List;
import kz.kegoc.bln.common.interfaces.HasId;

public interface Repository <T extends HasId> {
    List<T> selectAll();

    T selectById(Long entityId);

    T insert(T entity);

    T update(T entity);

    boolean delete(Long entityId);
}
