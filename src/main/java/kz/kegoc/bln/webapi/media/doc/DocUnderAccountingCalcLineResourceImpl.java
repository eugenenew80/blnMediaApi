package kz.kegoc.bln.webapi.media.doc;

import kz.kegoc.bln.entity.media.doc.DocUnderAccountingCalcLine;
import kz.kegoc.bln.entity.media.doc.dto.DocUnderAccountingCalcLineDto;
import kz.kegoc.bln.service.media.doc.DocUnderAccountingHeaderService;
import kz.kegoc.bln.service.media.doc.DocUnderAccountingCalcLineService;
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
public class DocUnderAccountingCalcLineResourceImpl {

	@GET
	public Response getAll(@PathParam("headerId") Long headerId) {
		List<DocUnderAccountingCalcLineDto> list = headerService.findById(headerId)
			.getCalcLines()
			.stream()
			.map( it-> mapper.map(it, DocUnderAccountingCalcLineDto.class) )
			.collect(Collectors.toList());		
	
		return Response.ok()
			.entity(new GenericEntity<Collection<DocUnderAccountingCalcLineDto>>(list){})
			.build();
	}


	@GET
	@Path("/{id : \\d+}")
	public Response getById(@PathParam("id") Long id) {
		DocUnderAccountingCalcLine entity = lineService.findById(id);
		return Response.ok()
			.entity(mapper.map(entity, DocUnderAccountingCalcLineDto.class))
			.build();
	}


	@POST
	public Response create(DocUnderAccountingCalcLineDto entityDto) {
		DocUnderAccountingCalcLine newEntity = lineService.create(mapper.map(entityDto, DocUnderAccountingCalcLine.class));
		return Response.ok()
			.entity(mapper.map(lineService.findById(newEntity.getId()), DocUnderAccountingCalcLineDto.class))
			.build();
	}


	@PUT
	@Path("{id : \\d+}")
	public Response update(@PathParam("id") Long id, DocUnderAccountingCalcLineDto entityDto ) {
		DocUnderAccountingCalcLine newEntity = lineService.update(mapper.map(entityDto, DocUnderAccountingCalcLine.class));
		return Response.ok()
			.entity(mapper.map(lineService.findById(newEntity.getId()), DocUnderAccountingCalcLineDto.class))
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
	private DocUnderAccountingCalcLineService lineService;

	@Inject
	private DozerBeanMapper mapper;
}
