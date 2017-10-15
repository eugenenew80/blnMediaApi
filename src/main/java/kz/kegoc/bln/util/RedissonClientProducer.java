package kz.kegoc.bln.util;

import javax.enterprise.inject.Produces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import kz.kegoc.bln.entity.media.DayMeteringDataRaw;
import kz.kegoc.bln.entity.media.HourMeteringDataRaw;
import kz.kegoc.bln.entity.media.MonthMeteringDataRaw;
import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RedissonClientProducer {
	private RedissonClient redissonClient = null;

	@Produces
	public RedissonClient createRedissonClient() {
		if (redissonClient !=null)
			return redissonClient;

		ObjectMapper mapper = new ObjectMapper()
		   .registerModule(new ParameterNamesModule())
		   .registerModule(new Jdk8Module())
		   .registerModule(new JavaTimeModule());

		Config config = new Config();
		config.useSingleServer().setAddress("redis://127.0.0.1:6379");
		config.setCodec(new JsonJacksonCodec(mapper));

		redissonClient = Redisson.create(config);
		return redissonClient;
	}


	@Produces
	public RBlockingQueue<HourMeteringDataRaw> hourMeteringDataQueue() {
		createRedissonClient();
		return redissonClient.getBlockingQueue("hourMeteringData");
	}


	@Produces
	public RBlockingQueue<DayMeteringDataRaw> dayMeteringDataQueue() {
		createRedissonClient();
		return redissonClient.getBlockingQueue("dayMeteringData");
	}


	@Produces
	public RBlockingQueue<MonthMeteringDataRaw> monthMeteringDataQueue() {
		createRedissonClient();
		return redissonClient.getBlockingQueue("monthMeteringData");
	}
}
