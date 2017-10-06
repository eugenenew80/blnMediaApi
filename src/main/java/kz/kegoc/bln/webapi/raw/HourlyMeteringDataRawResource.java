package kz.kegoc.bln.webapi.raw;

import kz.kegoc.bln.entity.media.HourlyMeteringDataRaw;
import kz.kegoc.bln.entity.media.WayEnteringData;
import kz.kegoc.bln.entity.media.MeteringDataStatus;
import kz.kegoc.bln.entity.media.dto.HourlyMeteringDataRawDto;
import kz.kegoc.bln.entity.media.dto.HourlyMeteringDataRawListDto;
import kz.kegoc.bln.queue.raw.HourlyMeteringDataRawQueue;
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
@Path("/raw/hourlyData")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class HourlyMeteringDataRawResource {

	public HourlyMeteringDataRawResource() {
		mapper = new DozerBeanMapper();
		mapper.setMappingFiles(Arrays.asList("mapping/raw/HourlyMeteringDataRawDtoDefaultMapping.xml"));
	}

	
	@POST
	public Response create(HourlyMeteringDataRawDto entity) {
		entity.setWayEntering(WayEnteringData.USER);
		entity.setStatus(MeteringDataStatus.DRAFT);
		entity.setDataSourceCode("MANUAL");
		
		service.addMeteringData(mapper.map(entity, HourlyMeteringDataRaw.class));
		return Response.ok().build();
	}	

	@POST
	@Path("/list")
	public Response createAll(HourlyMeteringDataRawListDto listDto) {
		List<HourlyMeteringDataRaw> list = listDto.getMeteringData().stream()
			.map(t -> {
				t.setMeteringDate(listDto.getMeteringDate());
				t.setMeteringPointCode(listDto.getMeteringPointCode());
				t.setWayEntering(WayEnteringData.USER);
				t.setStatus(MeteringDataStatus.DRAFT);
				t.setDataSourceCode("MANUAL");
				return mapper.map(t, HourlyMeteringDataRaw.class);
			})
			.collect(Collectors.toList());

		service.addMeteringListData(list);
		return Response.ok().build();
	}


	@Inject
	private HourlyMeteringDataRawQueue service;
	private DozerBeanMapper mapper;
}
