package kz.kegoc.bln.webapi.exception.mapper;

import javax.ejb.EJBException;
import javax.transaction.RollbackException;
import javax.validation.ValidationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import kz.kegoc.bln.exception.ApplicationException;
import kz.kegoc.bln.webapi.exception.entity.ErrorMessage;
import org.hibernate.exception.SQLGrammarException;


@Provider
public class EjbExceptionMapperImpl implements ExceptionMapper<EJBException> { 
	
    @Override
    public Response toResponse(EJBException exc) {
		if (exc.getCause()!=null) {
			/*
			if (exc.getCause() instanceof RollbackException) {
				Throwable appExc =  exc.getCause().getCause().getCause().getCause();
				//System.out.println("QQQQQ EjbExceptionMapperImpl " + appExc.getClass());

				return Response.status(500)
					.type(MediaType.APPLICATION_JSON)
					.entity(new ErrorMessage("tx-exception", appExc.getMessage()))
					.build();
			}
			*/

			if (exc.getCause() instanceof SQLGrammarException) {
				Throwable appExc =  exc.getCause().getCause();
				return Response.status(500)
					.type(MediaType.APPLICATION_JSON)
					.entity(new ErrorMessage("sql-exception", appExc.getMessage()))
					.build();
			}

			if (exc.getCause() instanceof ValidationException) {
    			ValidationException appExc = (ValidationException) exc.getCause();
    	    	return Response.status(500)
    	    		.type(MediaType.APPLICATION_JSON)	
	    	        .entity(new ErrorMessage("validation-exception", appExc.getMessage()))
	    	        .build();    			
    		}

    		if (exc.getCause() instanceof ApplicationException) { 
    			ApplicationException appExc = (ApplicationException) exc.getCause();
    			
    			return Response.status(appExc.getStatusCode())
    				.type(MediaType.APPLICATION_JSON)	
	    	        .entity(new ErrorMessage(appExc.getCode(), appExc.getMessage()))
	    	        .build();    			
    		}
    		
        	return Response.status(500)
        		.type(MediaType.APPLICATION_JSON)	
    	        .entity(new ErrorMessage("ejb_exception", exc.getCause().getMessage()))
    	        .build();
    	}
    		
    	
    	return Response.status(500)
	        .entity(new ErrorMessage("ejb_exception", exc.getMessage()))
	        .build();
    }
}
