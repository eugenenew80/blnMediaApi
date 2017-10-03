package kz.kegoc.bln.webapi.media;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import kz.kegoc.bln.entity.media.dto.DailyMeteringDataDto;


@RequestScoped
@Path("/media/manualData")
@Produces({ "application/xml", "application/json" })
@Consumes({ "application/xml", "application/json" })
public class ManualDataResource {
	
	@POST
	public Response create(DailyMeteringDataDto data) {
		
		Config config = new Config();
		config.useSingleServer().setAddress("redis://127.0.0.1:6379");
		RedissonClient redisson = Redisson.create(config);
		
		RBlockingQueue<DailyMeteringDataDto> queue = redisson.getBlockingQueue("dailyMeteringData");
		queue.offer(data);
		
		return Response
				.ok()
				.build();
	}
}
