package kz.kegoc.bln.webapi.media.oper;

import kz.kegoc.bln.entity.media.oper.DocMeterReplacingLine;
import kz.kegoc.bln.entity.media.oper.dto.DocMeterReplacingLineDto;
import kz.kegoc.bln.service.media.oper.DocMeterReplacingHeaderService;
import kz.kegoc.bln.service.media.oper.DocMeterReplacingLineService;
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
		List<DocMeterReplacingLineDto> list = service.findByHeader(headerId)
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
		DocMeterReplacingLine entity = service.findById(id);
		return Response.ok()
				.entity(mapper.map(entity, DocMeterReplacingLineDto.class))
				.build();
	}


	@POST
	public Response create(DocMeterReplacingLineDto entityDto) {
		DocMeterReplacingLine newEntity = service.create(mapper.map(entityDto, DocMeterReplacingLine.class));
		return Response.ok()
				.entity(mapper.map(service.findById(newEntity.getId()), DocMeterReplacingLineDto.class))
				.build();
	}


	@PUT
	@Path("{id : \\d+}")
	public Response update(@PathParam("id") Long id, DocMeterReplacingLineDto entityDto ) {
		DocMeterReplacingLine newEntity = service.update(mapper.map(entityDto, DocMeterReplacingLine.class));
		return Response.ok()
				.entity(mapper.map(service.findById(newEntity.getId()), DocMeterReplacingLineDto.class))
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
	private DocMeterReplacingHeaderService meterReplacingHeaderService;

	@Inject
	private DocMeterReplacingLineService service;

	@Inject
	private DozerBeanMapper mapper;
}
