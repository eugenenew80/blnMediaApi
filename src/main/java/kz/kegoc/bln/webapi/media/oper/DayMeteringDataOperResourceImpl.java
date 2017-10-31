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

import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.oper.DayMeteringDataOper;
import kz.kegoc.bln.entity.media.oper.Group;
import kz.kegoc.bln.entity.media.oper.dto.DayMeteringDataOperDto;
import kz.kegoc.bln.entity.media.raw.DayMeteringBalanceRaw;
import kz.kegoc.bln.service.media.oper.DayMeteringDataOperService;
import kz.kegoc.bln.service.media.oper.GroupService;
import kz.kegoc.bln.service.media.raw.MeteringDataRawService;

@Stateless
@Path("/media/mediaDayMeteringDataOper")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class DayMeteringDataOperResourceImpl {

	@GET 
	@Path("/byGroup/{groupId : \\d+}") 
	public Response getByGroup(@PathParam("groupId") Long groupId, @QueryParam("operDate") String operDateStr ) {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime operDate = LocalDate.parse(operDateStr, dateFormatter).atStartOfDay();
		
		
		List<DayMeteringDataOperDto> list = null;
		Group group = groupService.findById(groupId);
		if (group!=null) {
			list = group.getMeteringPoints().stream()
				.map( g -> g.getMeteringPoint())
				.map(m -> {
					return m.getMeters().stream()
						.map(mr -> {
							DayMeteringDataOper d = new DayMeteringDataOper();
							d.setMeteringPoint(m);
							d.setMeter(mr.getMeter());
							
							DayMeteringBalanceRaw b = new DayMeteringBalanceRaw();
							b.setMeteringDate(operDate.minusDays(1));
							b.setExternalCode(m.getExternalCode());
							b.setDataSourceCode("EMCOS");
							b.setWayEntering(WayEntering.EMCOS);
							b.setUnitCode("kWh");
							b.setParamCode("AB+");
							b.setStatus(DataStatus.RAW);
							
							DayMeteringBalanceRaw balanceStart = dayMeteringBalanceService.findByEntity(b);
							System.out.println(balanceStart);
							
							return d;
						})
						.collect(Collectors.toList());
				})
				.flatMap(l -> l.stream())
				.map( it-> mapper.map(it, DayMeteringDataOperDto.class) )
				.collect(Collectors.toList());
		}
		
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
	private GroupService groupService;
	
	@Inject
	private MeteringDataRawService<DayMeteringBalanceRaw> dayMeteringBalanceService;
	
	@Inject
	private DozerBeanMapper mapper;
}
