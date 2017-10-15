package kz.kegoc.bln.webapi.media.raw;

import kz.kegoc.bln.entity.media.HourMeteringDataRaw;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.dto.HourMeteringDataRawDto;
import kz.kegoc.bln.entity.media.dto.HourMeteringDataRawListDto;
import kz.kegoc.bln.queue.MeteringDataQueueService;
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
@Path("/media/hour/day")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class HourMeteringDataRawResource {

	public HourMeteringDataRawResource() {
		mapper = new DozerBeanMapper();
		mapper.setMappingFiles(Arrays.asList("mapping/raw/HourMeteringDataRawDtoDefaultMapping.xml"));
	}

	
	@POST
	public Response create(HourMeteringDataRawDto entity) {
		entity.setWayEntering(WayEntering.USER);
		entity.setStatus(DataStatus.RAW);
		entity.setDataSourceCode("MANUAL");
		
		service.add(mapper.map(entity, HourMeteringDataRaw.class));
		return Response.ok().build();
	}	

	@POST
	@Path("/list")
	public Response createAll(HourMeteringDataRawListDto listDto) {
		List<HourMeteringDataRaw> list = listDto.getMeteringData().stream()
			.map(t -> {
				t.setMeteringDate(listDto.getMeteringDate());
				t.setExternalCode(listDto.getExternalCode());
				t.setWayEntering(WayEntering.USER);
				t.setStatus(DataStatus.RAW);
				t.setDataSourceCode("MANUAL");
				return mapper.map(t, HourMeteringDataRaw.class);
			})
			.collect(Collectors.toList());

		service.addAll(list);
		return Response.ok().build();
	}


	@Inject
	private MeteringDataQueueService<HourMeteringDataRaw> service;
	private DozerBeanMapper mapper;
}
