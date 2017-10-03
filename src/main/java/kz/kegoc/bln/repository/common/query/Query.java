package kz.kegoc.bln.repository.common.query;

import java.util.Map;

public interface Query {
	String where();
	String orderBy();
	Map<String, MyQueryParam> params();
}
