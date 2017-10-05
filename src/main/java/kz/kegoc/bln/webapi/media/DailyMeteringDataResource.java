package kz.kegoc.bln.webapi.media;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.dozer.DozerBeanMapper;

import kz.kegoc.bln.entity.media.DailyMeteringData;
import kz.kegoc.bln.entity.media.WayEnteringData;
import kz.kegoc.bln.entity.media.MeteringDataStatus;
import kz.kegoc.bln.entity.media.dto.DailyMeteringDataDto;
import kz.kegoc.bln.service.queue.DailyMeteringDataQueueService;


@RequestScoped
@Path("/media/dailyData")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class DailyMeteringDataResource {
	
	public DailyMeteringDataResource() {
		mapper = new DozerBeanMapper();
		mapper.setMappingFiles(Arrays.asList("mapping/media/DailyMeteringDataDtoDefaultMapping.xml"));
	}

	
	@POST
	public Response create(DailyMeteringDataDto entity) {
		entity.setWayEntering(WayEnteringData.USER);
		entity.setStatus(MeteringDataStatus.DRAFT);
		entity.setDataSourceCode("MANUAL");
		
		service.addMeteringData(mapper.map(entity, DailyMeteringData.class));	
		return Response.ok()
				.build();
	}	

	@POST
	@Path("/list")
	public Response createAll(List<DailyMeteringDataDto> listDto) {
		
		List<DailyMeteringData> list = listDto.stream()
			.map(t-> { 
				t.setWayEntering(WayEnteringData.USER);
				t.setStatus(MeteringDataStatus.DRAFT);
				t.setDataSourceCode("MANUAL");
				return mapper.map(t, DailyMeteringData.class); 
			})
			.collect(Collectors.toList());
		
		service.addMeteringListData(list);		
		return Response.ok().build();
	}	


	@Inject
	private DailyMeteringDataQueueService service;
	private DozerBeanMapper mapper;
}
