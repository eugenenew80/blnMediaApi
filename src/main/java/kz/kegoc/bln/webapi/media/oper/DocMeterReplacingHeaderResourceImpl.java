package kz.kegoc.bln.webapi.media.oper;

import kz.kegoc.bln.entity.media.oper.DocMeterReplacingHeader;
import kz.kegoc.bln.entity.media.oper.dto.DocMeterReplacingHeaderDto;
import kz.kegoc.bln.mapper.EntityMapper;
import kz.kegoc.bln.repository.common.query.ConditionType;
import kz.kegoc.bln.repository.common.query.MyQueryParam;
import kz.kegoc.bln.repository.common.query.Query;
import kz.kegoc.bln.repository.common.query.QueryImpl;
import kz.kegoc.bln.service.media.oper.DocMeterReplacingHeaderService;
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
@Path("/impl/mediaDocMeterReplacingHeader")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class DocMeterReplacingHeaderResourceImpl {

	@GET
	public Response getAll(@QueryParam("name") String name) {
		Query query = QueryImpl.builder()
			.setParameter("name", isNotEmpty(name) ? new MyQueryParam("name", name + "%", ConditionType.LIKE) : null)
			.setOrderBy("t.id")
			.build();

		List<DocMeterReplacingHeaderDto> list = service.find(query)
			.stream()
			.map( it-> dtoMapper.map(it, DocMeterReplacingHeaderDto.class) )
			.collect(Collectors.toList());

		return Response.ok()
			.entity(new GenericEntity<Collection<DocMeterReplacingHeaderDto>>(list){})
			.build();
	}
	
	
	@GET 
	@Path("/{id : \\d+}") 
	public Response getById(@PathParam("id") Long id) {
		DocMeterReplacingHeader entity = service.findById(id);
		return Response.ok()
			.entity(dtoMapper.map(entity, DocMeterReplacingHeaderDto.class))
			.build();		
	}

	
	@POST
	public Response create(DocMeterReplacingHeaderDto entityDto) {
		DocMeterReplacingHeader entity = dtoMapper.map(entityDto, DocMeterReplacingHeader.class);
		entity = entityMapper.map(entity);
		DocMeterReplacingHeader newEntity = service.create(entity);

		return Response.ok()
			.entity(dtoMapper.map(newEntity, DocMeterReplacingHeaderDto.class))
			.build();
	}
	
	
	@PUT 
	@Path("{id : \\d+}") 
	public Response update(@PathParam("id") Long id, DocMeterReplacingHeaderDto entityDto ) {
		DocMeterReplacingHeader entity = dtoMapper.map(entityDto, DocMeterReplacingHeader.class);
		entity = entityMapper.map(entity);
		DocMeterReplacingHeader newEntity = service.update(entity);

		return Response.ok()
			.entity(dtoMapper.map(newEntity, DocMeterReplacingHeaderDto.class))
			.build();
	}
	
	
	@DELETE 
	@Path("{id : \\d+}") 
	public Response delete(@PathParam("id") Long id) {
		service.delete(id);		
		return Response.noContent()
			.build();
	}


	@Path("/{headerId : \\d+}/mediaDocMeterReplacingLine")
	public DocMeterReplacingLineResourceImpl getLineResource() {
		return lineResource;
	}
	

	@Inject
	private DocMeterReplacingHeaderService service;

	@Inject
	private DocMeterReplacingLineResourceImpl lineResource;

	@Inject
	private DozerBeanMapper dtoMapper;

	@Inject
	private EntityMapper<DocMeterReplacingHeader> entityMapper;
}
