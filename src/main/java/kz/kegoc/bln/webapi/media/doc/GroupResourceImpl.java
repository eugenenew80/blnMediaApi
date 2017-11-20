package kz.kegoc.bln.webapi.media.doc;

import java.util.*;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import kz.kegoc.bln.entity.media.Lang;
import kz.kegoc.bln.mapper.EntityMapper;
import kz.kegoc.bln.translator.Translator;
import org.dozer.DozerBeanMapper;
import kz.kegoc.bln.entity.media.doc.Group;
import kz.kegoc.bln.entity.media.doc.dto.GroupDto;
import kz.kegoc.bln.repository.common.query.*;
import kz.kegoc.bln.service.media.doc.GroupService;
import static org.apache.commons.lang3.StringUtils.*;

@Stateless
@Path("/media/mediaGroup")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class GroupResourceImpl {

	@GET 
	public Response getAll(@QueryParam("name") String name, @QueryParam("lang") Lang lang) {
		final Lang userLang = (lang!=null ? lang : defLang);

		Query query = QueryImpl.builder()			
			.setParameter("name", isNotEmpty(name) ? new MyQueryParam("name", name + "%", ConditionType.LIKE) : null)
			.setOrderBy("t.id")
			.build();		
		
		List<GroupDto> list = service.find(query)
			.stream()
			.map(it -> translator.translate(it, userLang))
			.map(it-> dtoMapper.map(it, GroupDto.class) )
			.collect(Collectors.toList());
		
		return Response.ok()
				.entity(new GenericEntity<Collection<GroupDto>>(list){})
				.build();
	}
	
	
	@GET 
	@Path("/{id : \\d+}") 
	public Response getById(@PathParam("id") Long id, @QueryParam("lang") Lang lang) {
		final Lang userLang = (lang!=null ? lang : defLang);

		Group entity = service.findById(id);
		return Response.ok()
			.entity(dtoMapper.map(translator.translate(entity, userLang), GroupDto.class))
			.build();		
	}
	

	@POST
	public Response create(GroupDto entityDto) {
		if (entityDto.getLang()==null)
			entityDto.setLang(defLang);

		Group entity = dtoMapper.map(entityDto, Group.class);
		entity = entityMapper.map(entity);
		Group newEntity = service.create(entity);

		return Response.ok()
			.entity(dtoMapper.map(translator.translate(newEntity, entityDto.getLang()), GroupDto.class))
			.build();
	}
	
	
	@PUT 
	@Path("{id : \\d+}") 
	public Response update(@PathParam("id") Long id, GroupDto entityDto ) {
		if (entityDto.getLang()==null)
			entityDto.setLang(defLang);

		Group entity = dtoMapper.map(entityDto, Group.class);
		entity = entityMapper.map(entity);
		Group newEntity = service.update(entity);

		return Response.ok()
			.entity(dtoMapper.map(translator.translate(newEntity, entityDto.getLang()), GroupDto.class))
			.build();
	}
	
	
	@DELETE 
	@Path("{id : \\d+}") 
	public Response delete(@PathParam("id") Long id) {
		service.delete(id);		
		return Response.noContent()
			.build();
	}
	

	@Path("/{groupId : \\d+}/mediaGroupMeteringPoint")
	public GroupMeteringPointResourceImpl getMeteringPoints(@PathParam("groupId") Long id) {
		return groupMeteringPointResource;
	}
	
	
	@Inject
	private GroupService service;

	@Inject
	private GroupMeteringPointResourceImpl groupMeteringPointResource;
	
	@Inject
	private DozerBeanMapper dtoMapper;

	@Inject
	private EntityMapper<Group> entityMapper;

	@Inject
	private Translator<Group> translator;

	@Inject
	private Lang defLang;
}
