package kz.kegoc.bln.webapi.media.oper;

import kz.kegoc.bln.entity.media.DataSource;
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
		List<DocMeteringReadingLineDto> list = headerService.findById(headerId)
			.getLines()
			.stream()
			.map( it-> mapper.map(it, DocMeteringReadingLineDto.class) )
			.collect(Collectors.toList());		
	
		return Response.ok()
			.entity(new GenericEntity<Collection<DocMeteringReadingLineDto>>(list){})
			.build();
	}


	@GET
	@Path("/autoFill")
	public Response autoFill(@PathParam("headerId") Long headerId) {
		List<DocMeteringReadingLineDto> list = headerService.autoFill(headerId)
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
		DocMeteringReadingLine entity = lineService.findById(id);
		return Response.ok()
			.entity(mapper.map(entity, DocMeteringReadingLineDto.class))
			.build();
	}


	@POST
	public Response create(DocMeteringReadingLineDto entityDto) {
		DocMeteringReadingLine entity = mapper.map(entityDto, DocMeteringReadingLine.class);
		DocMeteringReadingLine newEntity = lineService.create(entity);

		return Response.ok()
			.entity(mapper.map(newEntity, DocMeteringReadingLineDto.class))
			.build();
	}


	@PUT
	@Path("{id : \\d+}")
	public Response update(@PathParam("id") Long id, DocMeteringReadingLineDto entityDto ) {
		DocMeteringReadingLine entity = mapper.map(entityDto, DocMeteringReadingLine.class);
		entity.setDataSource(DataSource.DOC);
		DocMeteringReadingLine newEntity = lineService.update(entity);

		return Response.ok()
			.entity(mapper.map(newEntity, DocMeteringReadingLineDto.class))
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
	private DocMeteringReadingHeaderService headerService;

	@Inject
	private DocMeteringReadingLineService lineService;

	@Inject
	private DozerBeanMapper mapper;
}
