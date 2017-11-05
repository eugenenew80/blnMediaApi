package kz.kegoc.bln.webapi.media.oper;

import kz.kegoc.bln.entity.media.oper.DocMeteringReadingHeader;
import kz.kegoc.bln.entity.media.oper.dto.DocMeteringReadingHeaderDto;
import kz.kegoc.bln.repository.common.query.ConditionType;
import kz.kegoc.bln.repository.common.query.MyQueryParam;
import kz.kegoc.bln.repository.common.query.Query;
import kz.kegoc.bln.repository.common.query.QueryImpl;
import kz.kegoc.bln.service.media.oper.DocMeteringReadingHeaderService;
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
@Path("/media/mediaDocMeteringReadingHeader")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class DocMeteringReadingHeaderResourceImpl {

	@GET 
	public Response getAll(@QueryParam("name") String name) {		
		Query query = QueryImpl.builder()			
			.setParameter("name", isNotEmpty(name) ? new MyQueryParam("name", name + "%", ConditionType.LIKE) : null)
			.setOrderBy("t.id")
			.build();		
		
		List<DocMeteringReadingHeaderDto> list = service.find(query)
			.stream()
			.map( it-> mapper.map(it, DocMeteringReadingHeaderDto.class) )
			.collect(Collectors.toList());
		
		return Response.ok()
				.entity(new GenericEntity<Collection<DocMeteringReadingHeaderDto>>(list){})
				.build();
	}
	
	
	@GET 
	@Path("/{id : \\d+}") 
	public Response getById(@PathParam("id") Long id) {
		DocMeteringReadingHeader entity = service.findById(id);
		return Response.ok()
			.entity(mapper.map(entity, DocMeteringReadingHeaderDto.class))
			.build();		
	}
	
	
	@GET
	@Path("/byName/{name}")
	public Response getByName(@PathParam("name") String name) {		
		DocMeteringReadingHeader entity = service.findByName(name);
		return Response.ok()
			.entity(mapper.map(entity, DocMeteringReadingHeaderDto.class))
			.build();
	}

	
	@POST
	public Response create(DocMeteringReadingHeaderDto entityDto) {
		DocMeteringReadingHeader newEntity = service.create(mapper.map(entityDto, DocMeteringReadingHeader.class));
		return Response.ok()
			.entity(mapper.map(newEntity, DocMeteringReadingHeaderDto.class))
			.build();
	}
	
	
	@PUT 
	@Path("{id : \\d+}") 
	public Response update(@PathParam("id") Long id, DocMeteringReadingHeaderDto entityDto ) {
		DocMeteringReadingHeader newEntity = service.update(mapper.map(entityDto, DocMeteringReadingHeader.class));
		return Response.ok()
			.entity(mapper.map(newEntity, DocMeteringReadingHeaderDto.class))
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
	private DocMeteringReadingHeaderService service;

	@Inject
	private DozerBeanMapper mapper;
}
