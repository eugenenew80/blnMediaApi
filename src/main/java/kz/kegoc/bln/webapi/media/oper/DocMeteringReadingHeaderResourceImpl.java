package kz.kegoc.bln.webapi.media.oper;

import kz.kegoc.bln.entity.media.oper.DocMeteringReadingHeader;
import kz.kegoc.bln.entity.media.oper.dto.DocMeteringReadingHeaderDto;
import kz.kegoc.bln.entity.media.raw.Lang;
import kz.kegoc.bln.mapper.EntityMapper;
import kz.kegoc.bln.repository.common.query.ConditionType;
import kz.kegoc.bln.repository.common.query.MyQueryParam;
import kz.kegoc.bln.repository.common.query.Query;
import kz.kegoc.bln.repository.common.query.QueryImpl;
import kz.kegoc.bln.service.media.oper.DocMeteringReadingHeaderService;
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
@Path("/impl/mediaDocMeteringReadingHeader")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class DocMeteringReadingHeaderResourceImpl {
	private Lang defLang = Lang.RU;

	@GET 
	public Response getAll(@QueryParam("name") String name) {		
		Query query = QueryImpl.builder()			
			.setParameter("name", isNotEmpty(name) ? new MyQueryParam("name", name + "%", ConditionType.LIKE) : null)
			.setOrderBy("t.id")
			.build();		
		
		List<DocMeteringReadingHeaderDto> list = service.find(query)
			.stream()
			.map(it -> translator.translate(it, defLang))
			.map(it-> dtoMapper.map(it, DocMeteringReadingHeaderDto.class))
			.collect(Collectors.toList());
		
		return Response.ok()
				.entity(new GenericEntity<Collection<DocMeteringReadingHeaderDto>>(list){})
				.build();
	}
	
	
	@GET 
	@Path("/{id : \\d+}") 
	public Response getById(@PathParam("id") Long id) {
		DocMeteringReadingHeader entity = service.findById(id);
		return Response.ok()
			.entity(dtoMapper.map(translator.translate(entity, defLang), DocMeteringReadingHeaderDto.class))
			.build();		
	}

	
	@POST
	public Response create(DocMeteringReadingHeaderDto entityDto) {
		DocMeteringReadingHeader entity = dtoMapper.map(entityDto, DocMeteringReadingHeader.class);
		entity = entityMapper.map(entity);
		DocMeteringReadingHeader newEntity = service.create(entity);

		return Response.ok()
			.entity(dtoMapper.map(translator.translate(newEntity, defLang), DocMeteringReadingHeaderDto.class))
			.build();
	}
	
	
	@PUT 
	@Path("{id : \\d+}") 
	public Response update(@PathParam("id") Long id, DocMeteringReadingHeaderDto entityDto ) {
		DocMeteringReadingHeader entity = dtoMapper.map(entityDto, DocMeteringReadingHeader.class);
		entity = entityMapper.map(entity);
		DocMeteringReadingHeader newEntity = service.update(entity);

		return Response.ok()
			.entity(dtoMapper.map(translator.translate(newEntity, defLang), DocMeteringReadingHeaderDto.class))
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
}
