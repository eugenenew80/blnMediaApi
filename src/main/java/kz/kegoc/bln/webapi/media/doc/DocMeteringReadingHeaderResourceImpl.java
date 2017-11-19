package kz.kegoc.bln.webapi.media.doc;

import kz.kegoc.bln.entity.media.doc.DocMeteringReadingHeader;
import kz.kegoc.bln.entity.media.doc.dto.DocMeteringReadingHeaderDto;
import kz.kegoc.bln.entity.media.Lang;
import kz.kegoc.bln.mapper.EntityMapper;
import kz.kegoc.bln.repository.common.query.ConditionType;
import kz.kegoc.bln.repository.common.query.MyQueryParam;
import kz.kegoc.bln.repository.common.query.Query;
import kz.kegoc.bln.repository.common.query.QueryImpl;
import kz.kegoc.bln.service.media.doc.DocMeteringReadingHeaderService;
import kz.kegoc.bln.translator.Translator;
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
@Path("/media/mediaDocMeteringReadingHeader")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class DocMeteringReadingHeaderResourceImpl {

	@GET 
	public Response getAll(@QueryParam("name") String name, @QueryParam("lang") Lang lang) {
		final Lang userLang = (lang!=null ? lang : defLang);

		Query query = QueryImpl.builder()			
			.setParameter("name", isNotEmpty(name) ? new MyQueryParam("name", name + "%", ConditionType.LIKE) : null)
			.setOrderBy("t.id")
			.build();		
		
		List<DocMeteringReadingHeaderDto> list = service.find(query)
			.stream()
			.map(it -> translator.translate(it, userLang))
			.map(it-> dtoMapper.map(it, DocMeteringReadingHeaderDto.class))
			.collect(Collectors.toList());
		
		return Response.ok()
			.entity(new GenericEntity<Collection<DocMeteringReadingHeaderDto>>(list){})
			.build();
	}
	
	
	@GET 
	@Path("/{id : \\d+}") 
	public Response getById(@PathParam("id") Long id, @QueryParam("lang") Lang lang) {
		final Lang userLang = (lang!=null ? lang : defLang);
		DocMeteringReadingHeader entity = service.findById(id);
		return Response.ok()
			.entity(dtoMapper.map(translator.translate(entity, userLang), DocMeteringReadingHeaderDto.class))
			.build();		
	}

	
	@POST
	public Response create(DocMeteringReadingHeaderDto entityDto) {
		if (entityDto.getLang()==null)
			entityDto.setLang(defLang);

		DocMeteringReadingHeader entity = dtoMapper.map(entityDto, DocMeteringReadingHeader.class);
		entity = entityMapper.map(entity);
		DocMeteringReadingHeader newEntity = service.create(entity);

		return Response.ok()
			.entity(dtoMapper.map(translator.translate(newEntity, entityDto.getLang()), DocMeteringReadingHeaderDto.class))
			.build();
	}
	
	
	@PUT 
	@Path("{id : \\d+}") 
	public Response update(@PathParam("id") Long id, DocMeteringReadingHeaderDto entityDto ) {
		if (entityDto.getLang()==null)
			entityDto.setLang(defLang);

		DocMeteringReadingHeader entity = dtoMapper.map(entityDto, DocMeteringReadingHeader.class);
		entity = entityMapper.map(entity);
		DocMeteringReadingHeader newEntity = service.update(entity);

		return Response.ok()
			.entity(dtoMapper.map(translator.translate(newEntity, entityDto.getLang()), DocMeteringReadingHeaderDto.class))
			.build();
	}
	
	
	@DELETE 
	@Path("{id : \\d+}") 
	public Response delete(@PathParam("id") Long id) {
		service.delete(id);		
		return Response.noContent()
			.build();
	}


	@Path("/{headerId : \\d+}/mediaDocMeteringReadingLine")
	public DocMeteringReadingLineResourceImpl getLineResource() {
		return lineResource;
	}


	@Inject
	private DocMeteringReadingHeaderService service;

	@Inject
	private DocMeteringReadingLineResourceImpl lineResource;

	@Inject
	private DozerBeanMapper dtoMapper;

	@Inject
	private EntityMapper<DocMeteringReadingHeader> entityMapper;

	@Inject
	private Translator<DocMeteringReadingHeader> translator;

	@Inject
	private Lang defLang;
}
