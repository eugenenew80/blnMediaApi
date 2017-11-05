package kz.kegoc.bln.webapi.media.raw;

import kz.kegoc.bln.entity.media.DataSource;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.raw.dto.HourMeteringDataRawDto;
import kz.kegoc.bln.entity.media.raw.dto.HourMeteringDataRawListDto;
import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.raw.HourMeteringDataRaw;
import kz.kegoc.bln.queue.MeteringDataQueue;
import org.dozer.DozerBeanMapper;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Path("/media/hour/day")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class HourMeteringDataRawResourceImpl {

	@POST
	public Response create(HourMeteringDataRawDto entity) {
		entity.setWayEntering(WayEntering.API);
		entity.setStatus(DataStatus.RAW);
		entity.setDataSource(DataSource.MANUAL);
		
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
				t.setWayEntering(WayEntering.API);
				t.setStatus(DataStatus.RAW);
				t.setDataSource(DataSource.MANUAL);
				return mapper.map(t, HourMeteringDataRaw.class);
			})
			.collect(Collectors.toList());

		service.addAll(list);
		return Response.ok().build();
	}


	@Inject
	private MeteringDataQueue<HourMeteringDataRaw> service;

	@Inject
    private DozerBeanMapper mapper;
}
