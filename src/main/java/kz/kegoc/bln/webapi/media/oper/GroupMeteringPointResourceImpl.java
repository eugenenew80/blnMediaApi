package kz.kegoc.bln.webapi.media.oper;

import java.util.*;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import kz.kegoc.bln.entity.media.oper.GroupMeteringPoint;
import kz.kegoc.bln.service.media.oper.GroupMeteringPointService;
import org.dozer.DozerBeanMapper;
import kz.kegoc.bln.entity.media.oper.dto.GroupMeteringPointDto;
import kz.kegoc.bln.service.media.oper.GroupService;;

@Stateless
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class GroupMeteringPointResourceImpl {

	@GET
	public Response getAll(@PathParam("groupId") Long groupId) {
		List<GroupMeteringPointDto> list = groupService.findById(groupId)
			.getMeteringPoints()
			.stream()
			.map( it-> mapper.map(it, GroupMeteringPointDto.class) )
			.collect(Collectors.toList());		
	
		return Response.ok()
			.entity(new GenericEntity<Collection<GroupMeteringPointDto>>(list){})
			.build();
	}


	@GET
	@Path("/{id : \\d+}")
	public Response getById(@PathParam("id") Long id) {
		GroupMeteringPoint entity = service.findById(id);
		return Response.ok()
				.entity(mapper.map(entity, GroupMeteringPointDto.class))
				.build();
	}


	@POST
	public Response create(GroupMeteringPointDto entityDto) {
		GroupMeteringPoint newEntity = service.create(mapper.map(entityDto, GroupMeteringPoint.class));
		return Response.ok()
				.entity(mapper.map(service.findById(newEntity.getId()), GroupMeteringPointDto.class))
				.build();
	}


	@PUT
	@Path("{id : \\d+}")
	public Response update(@PathParam("id") Long id, GroupMeteringPointDto entityDto ) {
		GroupMeteringPoint newEntity = service.update(mapper.map(entityDto, GroupMeteringPoint.class));
		return Response.ok()
				.entity(mapper.map(service.findById(newEntity.getId()), GroupMeteringPointDto.class))
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
	private GroupService groupService;

	@Inject
	private GroupMeteringPointService service;

	@Inject
	private DozerBeanMapper mapper;
}
