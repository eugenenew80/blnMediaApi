package kz.kegoc.bln.webapi.exception.mapper;

import javax.validation.ValidationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import kz.kegoc.bln.webapi.exception.entity.ErrorMessage;

@Provider
public class ValidationExceptionMapperImpl implements ExceptionMapper<ValidationException> { 
	
    @Override
    public Response toResponse(ValidationException exc) {   	
    	return Response.status(500)
    		.type(MediaType.APPLICATION_JSON)
	        .entity(new ErrorMessage("validation-exception", exc.getMessage()))
	        .build();
    }
}
