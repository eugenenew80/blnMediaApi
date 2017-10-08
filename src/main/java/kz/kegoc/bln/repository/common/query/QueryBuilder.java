package kz.kegoc.bln.repository.common.query;

import kz.kegoc.bln.repository.common.query.QueryImpl.QueryBuliderImpl;

public interface QueryBuilder {
	 QueryBuilder setParameter(String field, Object value);
	 QueryBuilder setParameter(String field, MyQueryParam param);
	 QueryBuliderImpl setOrderBy(String orderBy);
	 Query build();
}
