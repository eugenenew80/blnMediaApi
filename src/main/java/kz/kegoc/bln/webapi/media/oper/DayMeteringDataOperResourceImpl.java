package kz.kegoc.bln.webapi.media.oper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.dozer.DozerBeanMapper;
import kz.kegoc.bln.entity.media.oper.DayMeteringDataOper;
import kz.kegoc.bln.entity.media.oper.dto.DayMeteringDataOperDto;
import kz.kegoc.bln.service.media.oper.DayMeteringDataOperService;

@Stateless
@Path("/media/mediaDayMeteringDataOper")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class DayMeteringDataOperResourceImpl {

	@GET 
	@Path("/byGroup/{groupId : \\d+}") 
	public Response getByGroup(@PathParam("groupId") Long groupId, @QueryParam("operDate") String operDateStr ) {
		LocalDateTime operDate = LocalDate.parse(operDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
		
		List<DayMeteringDataOperDto> list = service.findByGroup(groupId, operDate)
				.stream()
				.map( it-> mapper.map(it, DayMeteringDataOperDto.class) )
				.collect(Collectors.toList());
		
		return Response.ok()
				.entity(new GenericEntity<Collection<DayMeteringDataOperDto>>(list){})
				.build();		
	}
	

	@GET 
	@Path("/byGroup/{groupId : \\d+}/calc") 
	public Response calcByGroup(@PathParam("groupId") Long groupId, @QueryParam("operDate") String operDateStr ) {
		LocalDateTime operDate = LocalDate.parse(operDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
		
		List<DayMeteringDataOperDto> list = service.fillByGroup(groupId, operDate)
				.stream()
				.map( it-> mapper.map(it, DayMeteringDataOperDto.class) )
				.collect(Collectors.toList());
		
		return Response.ok()
				.entity(new GenericEntity<Collection<DayMeteringDataOperDto>>(list){})
				.build();		
	}
	
	@GET 
	@Path("/{id : \\d+}") 
	public Response getById(@PathParam("id") Long id) {
		DayMeteringDataOper entity = service.findById(id);
		return Response.ok()
			.entity(mapper.map(entity, DayMeteringDataOperDto.class))
			.build();		
	}
	
	
	@PUT 
	@Path("{id : \\d+}") 
	public Response update(@PathParam("id") Long id, DayMeteringDataOperDto entityDto ) {
		DayMeteringDataOper newEntity = service.update(mapper.map(entityDto, DayMeteringDataOper.class)); 
		return Response.ok()
			.entity(mapper.map(newEntity, DayMeteringDataOperDto.class))
			.build();
	}

	
	@Inject
	private DayMeteringDataOperService service;
	
	@Inject
	private DozerBeanMapper mapper;
}
