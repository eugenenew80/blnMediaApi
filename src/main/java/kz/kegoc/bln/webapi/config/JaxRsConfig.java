package kz.kegoc.bln.webapi.config;

import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import kz.kegoc.bln.webapi.exception.mapper.*;
import kz.kegoc.bln.webapi.raw.DailyMeteringDataRawResource;
import kz.kegoc.bln.webapi.raw.HourlyMeteringDataRawResource;
import kz.kegoc.bln.webapi.raw.MonthlyMeteringDataRawResource;


@ApplicationPath("/webapi")
public class JaxRsConfig extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		final Set<Class<?>> resources = new HashSet<Class<?>>();

		resources.add(MonthlyMeteringDataRawResource.class);
		resources.add(DailyMeteringDataRawResource.class);
		resources.add(HourlyMeteringDataRawResource.class);

		//resources.add(BasicAuthentificationFilter.class);
		resources.add(RepositryNotFoundExceptionMapperImpl.class);
		resources.add(EntityNotFoundExceptionMapperImpl.class);
		resources.add(DuplicateEntityExceptionMapperImpl.class);
		resources.add(ValidationExceptionMapperImpl.class);
		resources.add(InvalidArgumentExceptionMapperImpl.class);
		resources.add(EjbExceptionMapperImpl.class);
		resources.add(WebApplicationExceptionMapperImpl.class);
        resources.add(DefaultExceptionMapperImpl.class);
        
		return resources;
	}	
}
