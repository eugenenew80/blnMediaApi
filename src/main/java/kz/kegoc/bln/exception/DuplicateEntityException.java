package kz.kegoc.bln.exception;

public class DuplicateEntityException extends ApplicationException {
	private static final long serialVersionUID = -4472331634765720469L;
	
	public DuplicateEntityException() {
		super("Entity with id {0} is exist already");
	}

	public String getCode() { return code; }
	public int getStatusCode() { return statusCode; }
	
	private final String code = "duplicate-entity-exception";
	private final int statusCode = javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
}
