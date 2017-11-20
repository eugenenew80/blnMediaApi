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
import kz.kegoc.bln.entity.media.doc.DocType;
import kz.kegoc.bln.entity.media.doc.dto.DocTypeDto;
import kz.kegoc.bln.repository.common.query.*;
import kz.kegoc.bln.service.media.doc.DocTypeService;
import static org.apache.commons.lang3.StringUtils.*;

@Stateless
@Path("/media/mediaDocType")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class DocTypeResourceImpl {

	@GET 
	public Response getAll(@QueryParam("name") String name, @QueryParam("lang") Lang lang) {
		final Lang userLang = (lang!=null ? lang : defLang);

		Query query = QueryImpl.builder()			
			.setParameter("name", isNotEmpty(name) ? new MyQueryParam("name", name + "%", ConditionType.LIKE) : null)
			.setOrderBy("t.id")
			.build();		
		
		List<DocTypeDto> list = service.find(query)
			.stream()
			.map(it -> translator.translate(it, userLang))
			.map( it-> dtoMapper.map(it, DocTypeDto.class) )
			.collect(Collectors.toList());
		
		return Response.ok()
				.entity(new GenericEntity<Collection<DocTypeDto>>(list){})
				.build();
	}
	
	
	@GET 
	@Path("/{id : \\d+}") 
	public Response getById(@PathParam("id") Long id, @QueryParam("lang") Lang lang) {
		final Lang userLang = (lang!=null ? lang : defLang);

		DocType entity = service.findById(id);
		return Response.ok()
			.entity(dtoMapper.map(translator.translate(entity, userLang), DocTypeDto.class))
			.build();		
	}
	

	@POST
	public Response create(DocTypeDto entityDto) {
		if (entityDto.getLang()==null)
			entityDto.setLang(defLang);

		DocType entity = dtoMapper.map(entityDto, DocType.class);
		entity = entityMapper.map(entity);
		DocType newEntity = service.create(entity);

		return Response.ok()
			.entity(dtoMapper.map(newEntity, DocTypeDto.class))
			.build();
	}
	
	
	@PUT 
	@Path("{id : \\d+}") 
	public Response update(@PathParam("id") Long id, DocTypeDto entityDto ) {
		if (entityDto.getLang()==null)
			entityDto.setLang(defLang);

		DocType entity = dtoMapper.map(entityDto, DocType.class);
		entity = entityMapper.map(entity);
		DocType newEntity = service.update(entity);

		return Response.ok()
			.entity(dtoMapper.map(newEntity, DocTypeDto.class))
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
	private DocTypeService service;

	@Inject
	private DozerBeanMapper dtoMapper;

	@Inject
	private EntityMapper<DocType> entityMapper;

	@Inject
	private Translator<DocType> translator;

	@Inject
	private Lang defLang;

}
