package kz.kegoc.bln.webapi.media.doc;

import kz.kegoc.bln.entity.media.doc.DocMeterReplacingLine;
import kz.kegoc.bln.entity.media.doc.dto.DocMeterReplacingLineDto;
import kz.kegoc.bln.service.media.doc.DocMeterReplacingHeaderService;
import kz.kegoc.bln.service.media.doc.DocMeterReplacingLineService;
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
public class DocMeterReplacingLineResourceImpl {

	@GET
	public Response getAll(@PathParam("headerId") Long headerId) {
		List<DocMeterReplacingLineDto> list = headerService.findById(headerId)
			.getLines()
			.stream()
			.map( it-> mapper.map(it, DocMeterReplacingLineDto.class) )
			.collect(Collectors.toList());		
	
		return Response.ok()
			.entity(new GenericEntity<Collection<DocMeterReplacingLineDto>>(list){})
			.build();
	}


	@GET
	@Path("/{id : \\d+}")
	public Response getById(@PathParam("id") Long id) {
		DocMeterReplacingLine entity = lineService.findById(id);
		return Response.ok()
			.entity(mapper.map(entity, DocMeterReplacingLineDto.class))
			.build();
	}


	@POST
	public Response create(DocMeterReplacingLineDto entityDto) {
		DocMeterReplacingLine newEntity = lineService.create(mapper.map(entityDto, DocMeterReplacingLine.class));
		return Response.ok()
			.entity(mapper.map(newEntity, DocMeterReplacingLineDto.class))
			.build();
	}


	@PUT
	@Path("{id : \\d+}")
	public Response update(@PathParam("id") Long id, DocMeterReplacingLineDto entityDto ) {
		DocMeterReplacingLine newEntity = lineService.update(mapper.map(entityDto, DocMeterReplacingLine.class));
		return Response.ok()
			.entity(mapper.map(newEntity, DocMeterReplacingLineDto.class))
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
	private DocMeterReplacingHeaderService headerService;

	@Inject
	private DocMeterReplacingLineService lineService;

	@Inject
	private DozerBeanMapper mapper;
}