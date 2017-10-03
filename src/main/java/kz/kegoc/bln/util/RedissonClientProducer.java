package kz.kegoc.bln.util;

import javax.enterprise.inject.Produces;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RedissonClientProducer {
	
	@Produces
	public RedissonClient createRedissonClient() {
		Config config = new Config();
		config.useSingleServer().setAddress("redis://127.0.0.1:6379");
		RedissonClient redisson = Redisson.create(config);		
		return redisson;
	}
}
