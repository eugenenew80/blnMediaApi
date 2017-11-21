package kz.kegoc.bln.service.common;

import kz.kegoc.bln.entity.common.HasId;

public interface EntityHelperService<T extends HasId> {
    T addDependencies(T entity);

    T addTranslation(T entity);
}
