package kz.kegoc.bln.exception;

import java.text.MessageFormat;

public class EntityNotFoundException extends ApplicationException {
	private static final long serialVersionUID = 985373250774350883L;

	public EntityNotFoundException(Long entityId) {
		super(MessageFormat.format("Entity with id {0} is not exist", entityId));
		this.entityId = entityId;
		this.entityName = null;
	}

	public EntityNotFoundException(String entityName) {
		super(MessageFormat.format("Entity with name {0} is not exist", entityName));
		this.entityId = null;
		this.entityName = entityName;
	}

	public String getCode() { return code; }
	public int getStatusCode() { return statusCode; }
	public Long getEntityId() { return entityId; }
	public String getEntityName() { return entityName; }
	
	private final Long entityId;
	private final String entityName;
	private final String code = "entity-not-found-exception";
	private final int statusCode = javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
}
