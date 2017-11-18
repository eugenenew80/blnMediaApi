package kz.kegoc.bln.webapi.config;

import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import kz.kegoc.bln.ejb.jackson.ObjectMapperContextResolver;
import kz.kegoc.bln.webapi.exception.mapper.*;
import kz.kegoc.bln.webapi.filters.BasicAuthentificationFilter;
import kz.kegoc.bln.webapi.media.oper.*;

@ApplicationPath("/webapi")
public class JaxRsConfig extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		final Set<Class<?>> resources = new HashSet<Class<?>>();

		resources.add(GroupResourceImpl.class);
		resources.add(GroupMeteringPointResourceImpl.class);
		resources.add(DocTypeResourceImpl.class);
		resources.add(DocMeteringReadingHeaderResourceImpl.class);
		resources.add(DocMeterReplacingHeaderResourceImpl.class);
		resources.add(DocUnderAccountingHeaderResourceImpl.class);

		resources.add(BasicAuthentificationFilter.class);
		resources.add(RepositryNotFoundExceptionMapperImpl.class);
		resources.add(EntityNotFoundExceptionMapperImpl.class);
		resources.add(DuplicateEntityExceptionMapperImpl.class);
		resources.add(ValidationExceptionMapperImpl.class);
		resources.add(InvalidArgumentExceptionMapperImpl.class);
		resources.add(EjbExceptionMapperImpl.class);
		resources.add(WebApplicationExceptionMapperImpl.class);
        resources.add(DefaultExceptionMapperImpl.class);

		resources.add(ObjectMapperContextResolver.class);
		return resources;
	}	
}
