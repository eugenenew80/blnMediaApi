package kz.kegoc.bln.webapi.media.oper;

import java.util.*;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.dozer.DozerBeanMapper;
import kz.kegoc.bln.entity.media.oper.DocType;
import kz.kegoc.bln.entity.media.oper.dto.DocTypeDto;
import kz.kegoc.bln.repository.common.query.*;
import kz.kegoc.bln.service.media.oper.DocTypeService;
import static org.apache.commons.lang3.StringUtils.*;

@Stateless
@Path("/media/mediaDocType")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class DocTypeResourceImpl {

	@GET 
	public Response getAll(@QueryParam("name") String name) {		
		Query query = QueryImpl.builder()			
			.setParameter("name", isNotEmpty(name) ? new MyQueryParam("name", name + "%", ConditionType.LIKE) : null)
			.setOrderBy("t.id")
			.build();		
		
		List<DocTypeDto> list = service.find(query)
			.stream()
			.map( it-> mapper.map(it, DocTypeDto.class) )
			.collect(Collectors.toList());
		
		return Response.ok()
				.entity(new GenericEntity<Collection<DocTypeDto>>(list){})
				.build();
	}
	
	
	@GET 
	@Path("/{id : \\d+}") 
	public Response getById(@PathParam("id") Long id) {
		DocType entity = service.findById(id);
		return Response.ok()
			.entity(mapper.map(entity, DocTypeDto.class))
			.build();		
	}
	
	
	@GET
	@Path("/byName/{name}")
	public Response getByName(@PathParam("name") String name) {		
		DocType entity = service.findByName(name);
		return Response.ok()
			.entity(mapper.map(entity, DocTypeDto.class))
			.build();
	}

	
	@POST
	public Response create(DocTypeDto entityDto) {
		DocType newEntity = service.create(mapper.map(entityDto, DocType.class));	
		return Response.ok()
			.entity(mapper.map(newEntity, DocTypeDto.class))
			.build();
	}
	
	
	@PUT 
	@Path("{id : \\d+}") 
	public Response update(@PathParam("id") Long id, DocTypeDto entityDto ) {
		DocType newEntity = service.update(mapper.map(entityDto, DocType.class)); 
		return Response.ok()
			.entity(mapper.map(newEntity, DocTypeDto.class))
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
	private DocTypeService service;

	@Inject
	private DozerBeanMapper mapper;
}
