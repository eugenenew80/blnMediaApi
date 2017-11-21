package kz.kegoc.bln.webapi.media.doc;

import kz.kegoc.bln.entity.doc.DocUnderAccountingMeasLine;
import kz.kegoc.bln.entity.doc.dto.DocUnderAccountingMeasLineDto;
import kz.kegoc.bln.service.doc.DocUnderAccountingHeaderService;
import kz.kegoc.bln.service.doc.DocUnderAccountingMeasLineService;
import org.dozer.DozerBeanMapper;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Stateless
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class DocUnderAccountingMeasLineResourceImpl {

	@GET
	public Response getAll(@PathParam("headerId") Long headerId) {
		List<DocUnderAccountingMeasLineDto> list = headerService.findById(headerId)
			.getMeasLines()
			.stream()
			.map( it-> mapper.map(it, DocUnderAccountingMeasLineDto.class) )
			.collect(Collectors.toList());		
	
		return Response.ok()
			.entity(new GenericEntity<Collection<DocUnderAccountingMeasLineDto>>(list){})
			.build();
	}


	@GET
	@Path("/{id : \\d+}")
	public Response getById(@PathParam("id") Long id) {
		DocUnderAccountingMeasLine entity = lineService.findById(id);
		return Response.ok()
			.entity(mapper.map(entity, DocUnderAccountingMeasLineDto.class))
			.build();
	}


	@POST
	public Response create(DocUnderAccountingMeasLineDto entityDto) {
		DocUnderAccountingMeasLine newEntity = lineService.create(mapper.map(entityDto, DocUnderAccountingMeasLine.class));
		return Response.ok()
			.entity(mapper.map(lineService.findById(newEntity.getId()), DocUnderAccountingMeasLineDto.class))
			.build();
	}


	@PUT
	@Path("{id : \\d+}")
	public Response update(@PathParam("id") Long id, DocUnderAccountingMeasLineDto entityDto ) {
		DocUnderAccountingMeasLine newEntity = lineService.update(mapper.map(entityDto, DocUnderAccountingMeasLine.class));
		return Response.ok()
			.entity(mapper.map(lineService.findById(newEntity.getId()), DocUnderAccountingMeasLineDto.class))
			.build();
	}


	@DELETE
	@Path("{id : \\d+}")
	public Response delete(@PathParam("id") Long id) {
		lineService.delete(id);
		return Response.noContent()
			.build();
	}


	@Inject
	private DocUnderAccountingHeaderService headerService;

	@Inject
	private DocUnderAccountingMeasLineService lineService;

	@Inject
	private DozerBeanMapper mapper;
}
