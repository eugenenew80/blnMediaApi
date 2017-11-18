package kz.kegoc.bln.mapper;

import kz.kegoc.bln.entity.common.HasId;

public interface EntityMapper<T extends HasId> {
    T map(T entity);
}
