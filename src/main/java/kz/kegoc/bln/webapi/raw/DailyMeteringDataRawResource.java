package kz.kegoc.bln.webapi.raw;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.dozer.DozerBeanMapper;

import kz.kegoc.bln.entity.media.DailyMeteringDataRaw;
import kz.kegoc.bln.entity.media.WayEnteringData;
import kz.kegoc.bln.entity.media.MeteringDataStatus;
import kz.kegoc.bln.entity.media.dto.DailyMeteringDataRawDto;
import kz.kegoc.bln.queue.raw.DailyMeteringDataRawQueue;


@RequestScoped
@Path("/raw/dailyData")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class DailyMeteringDataRawResource {
	
	public DailyMeteringDataRawResource() {
		mapper = new DozerBeanMapper();
		mapper.setMappingFiles(Arrays.asList("mapping/raw/DailyMeteringDataRawDtoDefaultMapping.xml"));
	}

	
	@POST
	public Response create(DailyMeteringDataRawDto entity) {
		entity.setWayEntering(WayEnteringData.USER);
		entity.setStatus(MeteringDataStatus.DRAFT);
		entity.setDataSourceCode("MANUAL");
		
		service.addMeteringData(mapper.map(entity, DailyMeteringDataRaw.class));
		return Response.ok()
				.build();
	}	

	@POST
	@Path("/list")
	public Response createAll(List<DailyMeteringDataRawDto> listDto) {
		
		List<DailyMeteringDataRaw> list = listDto.stream()
			.map(t-> { 
				t.setWayEntering(WayEnteringData.USER);
				t.setStatus(MeteringDataStatus.DRAFT);
				t.setDataSourceCode("MANUAL");
				return mapper.map(t, DailyMeteringDataRaw.class);
			})
			.collect(Collectors.toList());
		
		service.addMeteringListData(list);		
		return Response.ok().build();
	}	


	@Inject
	private DailyMeteringDataRawQueue service;
	private DozerBeanMapper mapper;
}
