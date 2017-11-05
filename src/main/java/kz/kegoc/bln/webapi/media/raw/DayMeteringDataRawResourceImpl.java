package kz.kegoc.bln.webapi.media.raw;

import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import kz.kegoc.bln.entity.media.DataSource;
import kz.kegoc.bln.queue.MeteringDataQueue;
import org.dozer.DozerBeanMapper;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.raw.DayMeteringDataRaw;
import kz.kegoc.bln.entity.media.raw.dto.DayMeteringDataRawDto;
import kz.kegoc.bln.entity.media.DataStatus;

@Stateless
@Path("/media/raw/day")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class DayMeteringDataRawResourceImpl {

	@POST
	public Response create(DayMeteringDataRawDto entity) {
		entity.setWayEntering(WayEntering.API);
		entity.setStatus(DataStatus.RAW);
		entity.setDataSource(DataSource.MANUAL);
		
		service.add(mapper.map(entity, DayMeteringDataRaw.class));
		return Response.ok()
				.build();
	}	

	@POST
	@Path("/list")
	public Response createAll(List<DayMeteringDataRawDto> listDto) {
		
		List<DayMeteringDataRaw> list = listDto.stream()
			.map(t-> { 
				t.setWayEntering(WayEntering.API);
				t.setStatus(DataStatus.RAW);
				t.setDataSource(DataSource.MANUAL);
				return mapper.map(t, DayMeteringDataRaw.class);
			})
			.collect(Collectors.toList());
		
		service.addAll(list);
		return Response.ok().build();
	}	


	@Inject
	private MeteringDataQueue<DayMeteringDataRaw> service;

	@Inject
    private DozerBeanMapper mapper;
}
