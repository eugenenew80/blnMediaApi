package kz.kegoc.bln.webapi.exception.mapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import kz.kegoc.bln.exception.DuplicateEntityException;
import kz.kegoc.bln.webapi.exception.entity.ErrorMessage;


@Provider
public class DuplicateEntityExceptionMapperImpl implements ExceptionMapper<DuplicateEntityException> { 
	
    @Override
    public Response toResponse(DuplicateEntityException exc) {
    	return Response.status(exc.getStatusCode())
    		.type(MediaType.APPLICATION_JSON)
    		.entity(new ErrorMessage(exc.getCode(), exc.getMessage()))
	        .build();
    }
}
