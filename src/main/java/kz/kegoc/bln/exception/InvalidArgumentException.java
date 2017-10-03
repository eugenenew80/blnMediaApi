package kz.kegoc.bln.exception;


public class InvalidArgumentException extends ApplicationException {
	private static final long serialVersionUID = 3265342949146864617L;
	
	public InvalidArgumentException() {
		super("Invalid argument exception");
	}
	

	public InvalidArgumentException(Object entity) {
		super("Invalid argument exception");
		this.entity = entity;
	}

	public InvalidArgumentException(Object entity, String message) {
		super(message);
		this.entity = entity;
	}
	
	public String getCode() { return code; }
	public int getStatusCode() { return statusCode; }
	public Object getEntity() { return entity; }

	private final String code = "invalid-argument-exception";
	private final int statusCode = javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
	private Object entity;
}
