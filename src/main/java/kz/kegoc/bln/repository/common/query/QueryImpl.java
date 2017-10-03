package kz.kegoc.bln.repository.common.query;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors; 


public class QueryImpl implements Query {
	private final String where;
	private final String orderBy;
	private final Map<String, MyQueryParam> params;
	
	public String where() {
		return where;
	}

	
	public String orderBy() {
		return orderBy;
	}
	
	
	public Map<String, MyQueryParam> params() {
		return Collections.unmodifiableMap(params);
	}
	
	
	private QueryImpl(Map<String, MyQueryParam> fields, String orderBy) {
		where = fields.keySet().stream()
					.map( key -> "t." + key.substring(0, key.indexOf("@"))  + " " + fields.get(key).conditionType.toString() + " "  + ":" + fields.get(key).field )
					.collect(Collectors.joining(" and "))
					.toString();
		
		params = fields.keySet().stream()
	    			.collect(Collectors.toMap(key -> fields.get(key).field, fields::get));
		
		this.orderBy = orderBy;
	}
	
	
	public static QueryBuilder builder() {
		return new QueryBuliderImpl();
	}
	

	public static class QueryBuliderImpl implements QueryBuilder {
		
		public QueryBuliderImpl setParameter(String field, Object value) {
			return setParameter(field, new MyQueryParam(field, value, ConditionType.EQUALS));
		}
		

		public QueryBuliderImpl setParameter(String field, MyQueryParam param) {
			if (param!=null && param.value!=null)
				params.put(field + "@" + param.field, param);
			return this;
		}
		

		public QueryBuliderImpl setOrderBy(String orderBy) {
			this.orderBy = "";
			if (orderBy!=null && !orderBy.equals(""))
				this.orderBy = "order by " + orderBy; 
			return this;
		}

		
		public Query build() {
			return new QueryImpl(params, orderBy);
		}
		
		private final Map<String, MyQueryParam> params = new HashMap<>();
		private String orderBy = ""; 
	}
	
}
