package kz.kegoc.bln.repository.common;

import java.util.List;
import kz.kegoc.bln.entity.common.HasId;
import kz.kegoc.bln.repository.common.query.Query;


public interface Repository <T extends HasId> {
    List<T> selectAll();

    List<T> select(Query query);
    
    T selectById(Long entityId);
    
    T selectByCode(String entitycode);
    
    T selectByName(String entityName);
    
    T insert(T entity);

    T update(T entity);

    boolean delete(Long entityId);
}
