package kz.kegoc.bln.exception;

public class RepositoryNotFoundException extends ApplicationException {
	private static final long serialVersionUID = 7805786431835780607L;
	
	public RepositoryNotFoundException() {
		super("Repository not found");
	}
	
	public String getCode() { return code; }
	public int getStatusCode() { return statusCode; }

	private final String code = "repository-not-found-exception";
	private final int statusCode = javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
}
