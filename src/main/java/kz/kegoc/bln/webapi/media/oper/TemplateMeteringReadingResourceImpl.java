package kz.kegoc.bln.webapi.media.oper;

import kz.kegoc.bln.entity.media.oper.TemplateMeteringReading;
import kz.kegoc.bln.entity.media.oper.dto.TemplateMeteringReadingDto;
import kz.kegoc.bln.repository.common.query.ConditionType;
import kz.kegoc.bln.repository.common.query.MyQueryParam;
import kz.kegoc.bln.repository.common.query.Query;
import kz.kegoc.bln.repository.common.query.QueryImpl;
import kz.kegoc.bln.service.media.oper.TemplateMeteringReadingService;
import org.dozer.DozerBeanMapper;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Stateless
@Path("/media/mediaTemplateMeteringReading")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class TemplateMeteringReadingResourceImpl {

	@GET 
	public Response getAll(@QueryParam("name") String name) {		
		Query query = QueryImpl.builder()			
			.setParameter("name", isNotEmpty(name) ? new MyQueryParam("name", name + "%", ConditionType.LIKE) : null)
			.setOrderBy("t.id")
			.build();		
		
		List<TemplateMeteringReadingDto> list = service.find(query)
			.stream()
			.map( it-> mapper.map(it, TemplateMeteringReadingDto.class) )
			.collect(Collectors.toList());
		
		return Response.ok()
				.entity(new GenericEntity<Collection<TemplateMeteringReadingDto>>(list){})
				.build();
	}
	
	
	@GET 
	@Path("/{id : \\d+}") 
	public Response getById(@PathParam("id") Long id) {
		TemplateMeteringReading entity = service.findById(id);
		return Response.ok()
			.entity(mapper.map(entity, TemplateMeteringReadingDto.class))
			.build();		
	}
	
	
	@GET
	@Path("/byName/{name}")
	public Response getByName(@PathParam("name") String name) {		
		TemplateMeteringReading entity = service.findByName(name);
		return Response.ok()
			.entity(mapper.map(entity, TemplateMeteringReadingDto.class))
			.build();
	}

	
	@POST
	public Response create(TemplateMeteringReadingDto entityDto) {
		TemplateMeteringReading newEntity = service.create(mapper.map(entityDto, TemplateMeteringReading.class));
		return Response.ok()
			.entity(mapper.map(newEntity, TemplateMeteringReadingDto.class))
			.build();
	}
	
	
	@PUT 
	@Path("{id : \\d+}") 
	public Response update(@PathParam("id") Long id, TemplateMeteringReadingDto entityDto ) {
		TemplateMeteringReading newEntity = service.update(mapper.map(entityDto, TemplateMeteringReading.class));
		return Response.ok()
			.entity(mapper.map(newEntity, TemplateMeteringReadingDto.class))
			.build();
	}
	
	
	@DELETE 
	@Path("{id : \\d+}") 
	public Response delete(@PathParam("id") Long id) {
		service.delete(id);		
		return Response.noContent()
			.build();
	}

	
	
	@Inject
	private TemplateMeteringReadingService service;

	@Inject
	private DozerBeanMapper mapper;
}
