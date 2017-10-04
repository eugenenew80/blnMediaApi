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
import kz.kegoc.bln.entity.media.MeteringDataStatus;
import kz.kegoc.bln.entity.media.dto.DailyMeteringDataDto;
import kz.kegoc.bln.service.media.ManualDataService;


@RequestScoped
@Path("/media/manualData")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class ManualDataResource {
	
	public ManualDataResource() {
		mapper = new DozerBeanMapper();
		mapper.setMappingFiles(Arrays.asList("mapping/media/DailyMeteringDataDtoDefaultMapping.xml"));
	}

	
	@POST
	public Response create(DailyMeteringDataDto entity) {
		entity.setDataSourceCode("Manual");
		entity.setStatus(MeteringDataStatus.DRAFT);
		
		service.addMeteringData(mapper.map(entity, DailyMeteringData.class));	
		return Response.ok()
				.build();
	}	

	@POST
	@Path("/list")
	public Response createAll(List<DailyMeteringDataDto> listDto) {
		
		List<DailyMeteringData> list = listDto.stream()
			.map(t-> { 
				t.setDataSourceCode("Manual");
				t.setStatus(MeteringDataStatus.DRAFT);
				return mapper.map(t, DailyMeteringData.class); 
			})
			.collect(Collectors.toList());
		
		service.addMeteringListData(list);		
		return Response.ok()
				.build();
	}	
	
	@POST
	@Path("/shutdown")
	public Response shutdown() {
		service.shutdown();
		return Response.ok()
				.build();
	}	
	
	@Inject 
	private ManualDataService service;	
	private DozerBeanMapper mapper;
}
