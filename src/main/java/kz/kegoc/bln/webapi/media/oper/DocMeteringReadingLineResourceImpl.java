package kz.kegoc.bln.webapi.media.oper;

import kz.kegoc.bln.entity.media.oper.DocMeteringReadingLine;
import kz.kegoc.bln.entity.media.oper.dto.DocMeteringReadingLineDto;
import kz.kegoc.bln.service.media.oper.DocMeteringReadingHeaderService;
import kz.kegoc.bln.service.media.oper.DocMeteringReadingLineService;
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
public class DocMeteringReadingLineResourceImpl {

	@GET
	public Response getAll(@PathParam("headerId") Long headerId) {
		List<DocMeteringReadingLineDto> list = meteringReadingHeaderService.findById(headerId)
			.getLines()
			.stream()
			.map( it-> mapper.map(it, DocMeteringReadingLineDto.class) )
			.collect(Collectors.toList());		
	
		return Response.ok()
			.entity(new GenericEntity<Collection<DocMeteringReadingLineDto>>(list){})
			.build();
	}


	@GET
	@Path("/{id : \\d+}")
	public Response getById(@PathParam("id") Long id) {
		DocMeteringReadingLine entity = service.findById(id);
		return Response.ok()
				.entity(mapper.map(entity, DocMeteringReadingLineDto.class))
				.build();
	}


	@POST
	public Response create(DocMeteringReadingLineDto entityDto) {
		DocMeteringReadingLine newEntity = service.create(mapper.map(entityDto, DocMeteringReadingLine.class));
		return Response.ok()
				.entity(mapper.map(service.findById(newEntity.getId()), DocMeteringReadingLineDto.class))
				.build();
	}


	@PUT
	@Path("{id : \\d+}")
	public Response update(@PathParam("id") Long id, DocMeteringReadingLineDto entityDto ) {
		DocMeteringReadingLine newEntity = service.update(mapper.map(entityDto, DocMeteringReadingLine.class));
		return Response.ok()
				.entity(mapper.map(service.findById(newEntity.getId()), DocMeteringReadingLineDto.class))
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
	private DocMeteringReadingHeaderService meteringReadingHeaderService;

	@Inject
	private DocMeteringReadingLineService service;

	@Inject
	private DozerBeanMapper mapper;
}