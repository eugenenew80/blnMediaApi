package kz.kegoc.bln.exception;

public abstract class ApplicationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ApplicationException(String message) {
		super(message);
	}
	
	public abstract String getCode();
	public abstract int getStatusCode();	
}
