package kz.kegoc.bln.util;

import javax.enterprise.inject.Produces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.redisson.Redisson;
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

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());

		Config config = new Config();
		config.useSingleServer().setAddress("redis://127.0.0.1:6379");
		config.setCodec(new JsonJacksonCodec(mapper));

		redissonClient = Redisson.create(config);
		return redissonClient;
	}
}
