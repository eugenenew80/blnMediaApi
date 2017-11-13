package kz.kegoc.bln.webapi.media.oper;

import kz.kegoc.bln.entity.media.oper.DocTemplate;
import kz.kegoc.bln.entity.media.oper.dto.DocTemplateDto;
import kz.kegoc.bln.repository.common.query.ConditionType;
import kz.kegoc.bln.repository.common.query.MyQueryParam;
import kz.kegoc.bln.repository.common.query.Query;
import kz.kegoc.bln.repository.common.query.QueryImpl;
import kz.kegoc.bln.service.media.oper.DocTemplateService;
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
@Path("/media/mediaDocTemplate")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class DocTemplateResourceImpl {

	@GET 
	public Response getAll(@QueryParam("name") String name) {		
		Query query = QueryImpl.builder()
				.setParameter("name", isNotEmpty(name) ? new MyQueryParam("name", name + "%", ConditionType.LIKE) : null)
				.setOrderBy("t.id")
				.build();

		List<DocTemplateDto> list = service.find(query)
			.stream()
			.map( it-> mapper.map(it, DocTemplateDto.class) )
			.collect(Collectors.toList());
		
		return Response.ok()
				.entity(new GenericEntity<Collection<DocTemplateDto>>(list){})
				.build();
	}
	
	
	@GET 
	@Path("/{id : \\d+}") 
	public Response getById(@PathParam("id") Long id) {
		DocTemplate entity = service.findById(id);
		return Response.ok()
			.entity(mapper.map(entity, DocTemplateDto.class))
			.build();		
	}
	
	
	@GET
	@Path("/byName/{name}")
	public Response getByName(@PathParam("name") String name) {		
		DocTemplate entity = service.findByName(name);
		return Response.ok()
			.entity(mapper.map(entity, DocTemplateDto.class))
			.build();
	}

	
	@POST
	public Response create(DocTemplateDto entityDto) {
		DocTemplate newEntity = service.create(mapper.map(entityDto, DocTemplate.class));
		return Response.ok()
			.entity(mapper.map(newEntity, DocTemplateDto.class))
			.build();
	}
	
	
	@PUT 
	@Path("{id : \\d+}") 
	public Response update(@PathParam("id") Long id, DocTemplateDto entityDto ) {
		DocTemplate newEntity = service.update(mapper.map(entityDto, DocTemplate.class));
		return Response.ok()
			.entity(mapper.map(newEntity, DocTemplateDto.class))
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
	private DocTemplateService service;

	@Inject
	private DozerBeanMapper mapper;
}
