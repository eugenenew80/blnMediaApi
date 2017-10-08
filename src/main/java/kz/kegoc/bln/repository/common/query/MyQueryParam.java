package kz.kegoc.bln.repository.common.query;

public final class MyQueryParam {
	public final String field;
	public final Object value;
	public final ConditionType conditionType;
	
	public MyQueryParam(String field, Object value, ConditionType conditionType) {
		this.field = field;
		this.value = value;
		this.conditionType = conditionType;
	}
}
