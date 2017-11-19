package kz.kegoc.bln.webapi.media.doc;

import kz.kegoc.bln.entity.media.Lang;
import kz.kegoc.bln.entity.media.doc.DocUnderAccountingHeader;
import kz.kegoc.bln.entity.media.doc.dto.DocUnderAccountingHeaderDto;
import kz.kegoc.bln.mapper.EntityMapper;
import kz.kegoc.bln.repository.common.query.ConditionType;
import kz.kegoc.bln.repository.common.query.MyQueryParam;
import kz.kegoc.bln.repository.common.query.Query;
import kz.kegoc.bln.repository.common.query.QueryImpl;
import kz.kegoc.bln.service.media.doc.DocUnderAccountingHeaderService;
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
@Path("/media/mediaDocUnderAccountingHeader")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class DocUnderAccountingHeaderResourceImpl {

	@GET
	public Response getAll(@QueryParam("name") String name, @QueryParam("lang") Lang lang) {
		final Lang userLang = (lang!=null ? lang : defLang);

		Query query = QueryImpl.builder()
			.setParameter("name", isNotEmpty(name) ? new MyQueryParam("name", name + "%", ConditionType.LIKE) : null)
			.setOrderBy("t.id")
			.build();

		List<DocUnderAccountingHeaderDto> list = service.find(query)
			.stream()
			.map(it -> translator.translate(it, userLang))
			.map( it-> dtoMapper.map(it, DocUnderAccountingHeaderDto.class) )
			.collect(Collectors.toList());

		return Response.ok()
			.entity(new GenericEntity<Collection<DocUnderAccountingHeaderDto>>(list){})
			.build();
	}
	
	
	@GET 
	@Path("/{id : \\d+}") 
	public Response getById(@PathParam("id") Long id, @QueryParam("lang") Lang lang) {
		final Lang userLang = (lang!=null ? lang : defLang);
		DocUnderAccountingHeader entity = service.findById(id);
		return Response.ok()
			.entity(dtoMapper.map(translator.translate(entity, userLang), DocUnderAccountingHeaderDto.class))
			.build();		
	}
	

	@POST
	public Response create(DocUnderAccountingHeaderDto entityDto) {
		if (entityDto.getLang()==null)
			entityDto.setLang(defLang);

		DocUnderAccountingHeader entity = dtoMapper.map(entityDto, DocUnderAccountingHeader.class);
		entity = entityMapper.map(entity);
		DocUnderAccountingHeader newEntity = service.create(entity);

		return Response.ok()
			.entity(dtoMapper.map(translator.translate(newEntity, entityDto.getLang()), DocUnderAccountingHeaderDto.class))
			.build();
	}
	
	
	@PUT 
	@Path("{id : \\d+}") 
	public Response update(@PathParam("id") Long id, DocUnderAccountingHeaderDto entityDto ) {
		if (entityDto.getLang()==null)
			entityDto.setLang(defLang);

		DocUnderAccountingHeader entity = dtoMapper.map(entityDto, DocUnderAccountingHeader.class);
		entity = entityMapper.map(entity);
		DocUnderAccountingHeader newEntity = service.update(entity);

		return Response.ok()
			.entity(dtoMapper.map(translator.translate(newEntity, entityDto.getLang()), DocUnderAccountingHeaderDto.class))
			.build();
	}
	
	
	@DELETE 
	@Path("{id : \\d+}") 
	public Response delete(@PathParam("id") Long id) {
		service.delete(id);		
		return Response.noContent()
			.build();
	}


	@Path("/{headerId : \\d+}/mediaDocUnderAccountingMeasLine")
	public DocUnderAccountingMeasLineResourceImpl getMeasLineResource() {
		return measLineResource;
	}

	@Path("/{headerId : \\d+}/mediaDocUnderAccountingCalcLine")
	public DocUnderAccountingCalcLineResourceImpl getCalcLineResource() {
		return calcLineResource;
	}


	@Inject
	private DocUnderAccountingHeaderService service;

	@Inject
	private DocUnderAccountingMeasLineResourceImpl measLineResource;

	@Inject
	private DocUnderAccountingCalcLineResourceImpl calcLineResource;

	@Inject
	private DozerBeanMapper dtoMapper;

	@Inject
	private EntityMapper<DocUnderAccountingHeader> entityMapper;

	@Inject
	private Translator<DocUnderAccountingHeader> translator;

	@Inject
	private Lang defLang;
}
