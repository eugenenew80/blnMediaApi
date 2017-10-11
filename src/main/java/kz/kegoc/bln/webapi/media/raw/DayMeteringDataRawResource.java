package kz.kegoc.bln.webapi.media.raw;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.dozer.DozerBeanMapper;

import kz.kegoc.bln.entity.media.raw.DayMeteringDataRaw;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.raw.dto.DayMeteringDataRawDto;
import kz.kegoc.bln.queue.raw.DailyMeteringDataRawQueue;


@RequestScoped
@Path("/media/raw/day")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class DayMeteringDataRawResource {
	
	public DayMeteringDataRawResource() {
		mapper = new DozerBeanMapper();
		mapper.setMappingFiles(Arrays.asList("mapping/raw/DayMeteringDataRawDtoDefaultMapping.xml"));
	}

	
	@POST
	public Response create(DayMeteringDataRawDto entity) {
		entity.setWayEntering(WayEntering.USER);
		entity.setStatus(DataStatus.RAW);
		entity.setDataSourceCode("MANUAL");
		
		service.addMeteringData(mapper.map(entity, DayMeteringDataRaw.class));
		return Response.ok()
				.build();
	}	

	@POST
	@Path("/list")
	public Response createAll(List<DayMeteringDataRawDto> listDto) {
		
		List<DayMeteringDataRaw> list = listDto.stream()
			.map(t-> { 
				t.setWayEntering(WayEntering.USER);
				t.setStatus(DataStatus.RAW);
				t.setDataSourceCode("MANUAL");
				return mapper.map(t, DayMeteringDataRaw.class);
			})
			.collect(Collectors.toList());
		
		service.addMeteringListData(list);		
		return Response.ok().build();
	}	


	@Inject
	private DailyMeteringDataRawQueue service;
	private DozerBeanMapper mapper;
}
