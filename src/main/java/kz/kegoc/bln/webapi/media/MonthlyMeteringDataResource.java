package kz.kegoc.bln.webapi.media;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.dozer.DozerBeanMapper;

import kz.kegoc.bln.entity.media.MonthlyMeteringData;
import kz.kegoc.bln.entity.media.WayEnteringData;
import kz.kegoc.bln.entity.media.MeteringDataStatus;
import kz.kegoc.bln.entity.media.dto.MonthlyMeteringDataDto;
import kz.kegoc.bln.service.queue.MonthlyMeteringDataQueueService;


@RequestScoped
@Path("/media/monthlyData")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class MonthlyMeteringDataResource {
	
	public MonthlyMeteringDataResource() {
		mapper = new DozerBeanMapper();
		mapper.setMappingFiles(Arrays.asList("mapping/media/MonthlyMeteringDataDtoDefaultMapping.xml"));
	}

	
	@POST
	public Response create(MonthlyMeteringDataDto entity) {
		entity.setWayEntering(WayEnteringData.USER);
		entity.setStatus(MeteringDataStatus.DRAFT);
		entity.setDataSourceCode("MANUAL");
		
		service.addMeteringData(mapper.map(entity, MonthlyMeteringData.class));	
		return Response.ok()
				.build();
	}	

	@POST
	@Path("/list")
	public Response createAll(List<MonthlyMeteringDataDto> listDto) {
		
		List<MonthlyMeteringData> list = listDto.stream()
			.map(t-> { 
				t.setWayEntering(WayEnteringData.USER);
				t.setStatus(MeteringDataStatus.DRAFT);
				t.setDataSourceCode("MANUAL");
				return mapper.map(t, MonthlyMeteringData.class); 
			})
			.collect(Collectors.toList());
		
		service.addMeteringListData(list);		
		return Response.ok().build();
	}	
	
	@POST
	@Path("/shutdown")
	public Response shutdown() {
		service.shutdown();
		return Response.ok().build();
	}


	@POST
	@Path("/start")
	public Response start() {
		service.start();
		return Response.ok().build();
	}


	@Inject
	private MonthlyMeteringDataQueueService service;
	private DozerBeanMapper mapper;
}
