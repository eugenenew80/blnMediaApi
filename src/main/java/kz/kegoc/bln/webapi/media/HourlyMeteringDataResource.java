package kz.kegoc.bln.webapi.media;

import kz.kegoc.bln.entity.media.HourlyMeteringData;
import kz.kegoc.bln.entity.media.MeteringDataStatus;
import kz.kegoc.bln.entity.media.dto.HourlyMeteringDataDto;
import kz.kegoc.bln.service.media.HourlyMeteringDataService;
import org.dozer.DozerBeanMapper;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RequestScoped
@Path("/media/hourlyData")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class HourlyMeteringDataResource {

	public HourlyMeteringDataResource() {
		mapper = new DozerBeanMapper();
		mapper.setMappingFiles(Arrays.asList("mapping/media/HourlyMeteringDataDtoDefaultMapping.xml"));
	}

	
	@POST
	public Response create(HourlyMeteringDataDto entity) {
		entity.setDataSourceCode("Manual");
		entity.setStatus(MeteringDataStatus.DRAFT);
		
		service.addMeteringData(mapper.map(entity, HourlyMeteringData.class));
		return Response.ok()
				.build();
	}	

	@POST
	@Path("/list")
	public Response createAll(List<HourlyMeteringDataDto> listDto) {
		
		List<HourlyMeteringData> list = listDto.stream()
			.map(t-> { 
				t.setDataSourceCode("Manual");
				t.setStatus(MeteringDataStatus.DRAFT);
				return mapper.map(t, HourlyMeteringData.class);
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
	private HourlyMeteringDataService service;
	private DozerBeanMapper mapper;
}
