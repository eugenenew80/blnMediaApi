package kz.kegoc.bln.webapi.config;

import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import kz.kegoc.bln.ejb.jackson.ObjectMapperContextResolver;
import kz.kegoc.bln.webapi.exception.mapper.*;
import kz.kegoc.bln.webapi.filters.BasicAuthentificationFilter;
import kz.kegoc.bln.webapi.media.oper.DayMeteringDataOperResourceImpl;
import kz.kegoc.bln.webapi.media.oper.GroupMeteringPointResourceImpl;
import kz.kegoc.bln.webapi.media.oper.GroupResourceImpl;
import kz.kegoc.bln.webapi.media.raw.DayMeteringDataRawResourceImpl;
import kz.kegoc.bln.webapi.media.raw.HourMeteringDataRawResourceImpl;
import kz.kegoc.bln.webapi.media.raw.MonthMeteringDataRawResourceImpl;

@ApplicationPath("/webapi")
public class JaxRsConfig extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		final Set<Class<?>> resources = new HashSet<Class<?>>();

		resources.add(MonthMeteringDataRawResourceImpl.class);
		resources.add(DayMeteringDataRawResourceImpl.class);
		resources.add(HourMeteringDataRawResourceImpl.class);
		resources.add(GroupResourceImpl.class);
		resources.add(GroupMeteringPointResourceImpl.class);
		resources.add(DayMeteringDataOperResourceImpl.class);
		
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
