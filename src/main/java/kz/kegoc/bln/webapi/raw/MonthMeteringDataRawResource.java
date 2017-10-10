package kz.kegoc.bln.webapi.raw;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.dozer.DozerBeanMapper;

import kz.kegoc.bln.entity.media.raw.MonthMeteringDataRaw;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.raw.dto.MonthMeteringDataRawDto;
import kz.kegoc.bln.queue.raw.MonthlyMeteringDataRawQueue;


@RequestScoped
@Path("/media/month/day")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class MonthMeteringDataRawResource {
	
	public MonthMeteringDataRawResource() {
		mapper = new DozerBeanMapper();
		mapper.setMappingFiles(Arrays.asList("mapping/raw/MonthMeteringDataRawDtoDefaultMapping.xml"));
	}

	
	@POST
	public Response create(MonthMeteringDataRawDto entity) {
		entity.setWayEntering(WayEntering.USER);
		entity.setStatus(DataStatus.RAW);
		entity.setDataSourceCode("MANUAL");
		
		service.addMeteringData(mapper.map(entity, MonthMeteringDataRaw.class));
		return Response.ok()
				.build();
	}	

	@POST
	@Path("/list")
	public Response createAll(List<MonthMeteringDataRawDto> listDto) {
		
		List<MonthMeteringDataRaw> list = listDto.stream()
			.map(t-> { 
				t.setWayEntering(WayEntering.USER);
				t.setStatus(DataStatus.RAW);
				t.setDataSourceCode("MANUAL");
				return mapper.map(t, MonthMeteringDataRaw.class);
			})
			.collect(Collectors.toList());
		
		service.addMeteringListData(list);		
		return Response.ok().build();
	}	
	

	@Inject
	private MonthlyMeteringDataRawQueue service;
	private DozerBeanMapper mapper;
}
