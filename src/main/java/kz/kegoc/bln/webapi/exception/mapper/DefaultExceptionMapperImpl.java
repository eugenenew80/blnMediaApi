package kz.kegoc.bln.webapi.exception.mapper;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import kz.kegoc.bln.exception.ApplicationException;
import kz.kegoc.bln.webapi.exception.entity.ErrorMessage;


@Provider
public class DefaultExceptionMapperImpl implements ExceptionMapper<Throwable> { 
	
    @Override
    public Response toResponse(Throwable exc) {
    	Throwable causeException = exc;
    	if (exc.getCause()!=null) 
    		causeException = exc.getCause();
    		
    	String code = "";
    	String message = causeException.getMessage();
    	
    	if (causeException instanceof ApplicationException) {
    		code =  ((ApplicationException) causeException).getCode();
    		message =  ((ApplicationException) causeException).getMessage();
    	}    	

    	else if (causeException instanceof ConstraintViolationException) {
    		message = "Bean validation exception: ";
    		for (ConstraintViolation<?> v: ((ConstraintViolationException) causeException).getConstraintViolations()) { 
	    		message += v.getPropertyPath() + ": " +v.getMessage() + "; ";    	
    		}    				
		}
		else {
    		if (causeException.getCause()!=null) 
    			message = causeException.getCause().getMessage();
		}
    	
    	if (message==null || message.equals("")) {
    		code = "unknown";
    		message = causeException.getClass().getName();
    	}
    	
    	return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    		.type(MediaType.APPLICATION_JSON)
            .entity(new ErrorMessage(code, message))
            .build();
    }
}
