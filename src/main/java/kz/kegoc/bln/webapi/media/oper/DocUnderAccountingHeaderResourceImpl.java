package kz.kegoc.bln.webapi.media.oper;

import kz.kegoc.bln.entity.media.oper.DocUnderAccountingHeader;
import kz.kegoc.bln.entity.media.oper.DocType;
import kz.kegoc.bln.entity.media.oper.dto.DocUnderAccountingHeaderDto;
import kz.kegoc.bln.repository.common.query.ConditionType;
import kz.kegoc.bln.repository.common.query.MyQueryParam;
import kz.kegoc.bln.repository.common.query.Query;
import kz.kegoc.bln.repository.common.query.QueryImpl;
import kz.kegoc.bln.service.media.oper.DocUnderAccountingHeaderService;
import kz.kegoc.bln.service.media.oper.DocTypeService;
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
@Path("/media/mediaDocUnderAccountingHeader")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class DocUnderAccountingHeaderResourceImpl {

	@GET
	public Response getAll(@QueryParam("name") String name) {
		Query query = QueryImpl.builder()
			.setParameter("name", isNotEmpty(name) ? new MyQueryParam("name", name + "%", ConditionType.LIKE) : null)
			.setOrderBy("t.id")
			.build();

		List<DocUnderAccountingHeaderDto> list = service.find(query)
			.stream()
			.map( it-> dtoMapper.map(it, DocUnderAccountingHeaderDto.class) )
			.collect(Collectors.toList());

		return Response.ok()
			.entity(new GenericEntity<Collection<DocUnderAccountingHeaderDto>>(list){})
			.build();
	}
	
	
	@GET 
	@Path("/{id : \\d+}") 
	public Response getById(@PathParam("id") Long id) {
		DocUnderAccountingHeader entity = service.findById(id);
		return Response.ok()
			.entity(dtoMapper.map(entity, DocUnderAccountingHeaderDto.class))
			.build();		
	}
	

	@POST
	public Response create(DocUnderAccountingHeaderDto entityDto) {
		DocUnderAccountingHeader entity = dtoMapper.map(entityDto, DocUnderAccountingHeader.class);
		DocType docType = docTypeService.findById(3l);
		entity.setDocType(docType);

		DocUnderAccountingHeader newEntity = service.create(entity);
		return Response.ok()
			.entity(dtoMapper.map(newEntity, DocUnderAccountingHeaderDto.class))
			.build();
	}
	
	
	@PUT 
	@Path("{id : \\d+}") 
	public Response update(@PathParam("id") Long id, DocUnderAccountingHeaderDto entityDto ) {
		DocUnderAccountingHeader newEntity = service.update(dtoMapper.map(entityDto, DocUnderAccountingHeader.class));
		return Response.ok()
			.entity(dtoMapper.map(newEntity, DocUnderAccountingHeaderDto.class))
			.build();
	}
	
	
	@DELETE 
	@Path("{id : \\d+}") 
	public Response delete(@PathParam("id") Long id) {
		service.delete(id);		
		return Response.noContent()
			.build();
	}


	@Path("/{headerId : \\d+}/mediaDocUnderAccountingMeasLine")
	public DocUnderAccountingMeasLineResourceImpl getMeasLineResource() {
		return measLineResource;
	}

	@Path("/{headerId : \\d+}/mediaDocUnderAccountingCalcLine")
	public DocUnderAccountingCalcLineResourceImpl getCalcLineResource() {
		return calcLineResource;
	}


	@Inject
	private DocUnderAccountingHeaderService service;

	@Inject
	private DocTypeService docTypeService;

	@Inject
	private DocUnderAccountingMeasLineResourceImpl measLineResource;

	@Inject
	private DocUnderAccountingCalcLineResourceImpl calcLineResource;

	@Inject
	private DozerBeanMapper dtoMapper;
}
