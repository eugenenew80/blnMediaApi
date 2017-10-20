package kz.kegoc.bln.util;

import javax.enterprise.inject.Produces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import kz.kegoc.bln.annotation.ParamCodes;
import kz.kegoc.bln.entity.media.day.DayMeteringBalanceRaw;
import kz.kegoc.bln.entity.media.day.DayMeteringDataRaw;
import kz.kegoc.bln.entity.media.hour.HourMeteringDataRaw;
import kz.kegoc.bln.entity.media.month.MonthMeteringDataRaw;

import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RedissonClientProducer {
	private RedissonClient redissonClient = null;
	private RBlockingQueue<HourMeteringDataRaw> hourMeteringDataQueue = null;
	private RBlockingQueue<DayMeteringDataRaw> dayMeteringDataQueue  = null;
	private RBlockingQueue<MonthMeteringDataRaw> monthMeteringDataQueue  = null;
	private RBlockingQueue<DayMeteringBalanceRaw> dayMeteringBalanceQueue  = null;


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
	public RBlockingQueue<HourMeteringDataRaw> createHourMeteringDataQueue() {
		if (hourMeteringDataQueue!=null)
			return hourMeteringDataQueue;

		createRedissonClient();
		hourMeteringDataQueue = redissonClient.getBlockingQueue("hourMeteringData");
		return hourMeteringDataQueue;
	}


	@Produces
	public RBlockingQueue<DayMeteringDataRaw> createDayMeteringDataQueue() {
		if (dayMeteringDataQueue!=null)
			return dayMeteringDataQueue;

		createRedissonClient();
		dayMeteringDataQueue = redissonClient.getBlockingQueue("dayMeteringData");
		return dayMeteringDataQueue;
	}


	@Produces
	public RBlockingQueue<MonthMeteringDataRaw> createMonthMeteringDataQueue() {
		if (monthMeteringDataQueue!=null)
			return monthMeteringDataQueue;

		createRedissonClient();
		monthMeteringDataQueue = redissonClient.getBlockingQueue("monthMeteringData");
		return monthMeteringDataQueue;
	}
	
	
	@Produces
	public RBlockingQueue<DayMeteringBalanceRaw> createDayMeteringBalanceQueue() {
		if (dayMeteringBalanceQueue!=null)
			return dayMeteringBalanceQueue;

		createRedissonClient();
		dayMeteringBalanceQueue = redissonClient.getBlockingQueue("dayMeteringBalance");
		return dayMeteringBalanceQueue;
	}	
	
	
	@Produces
	@ParamCodes
	public Map<String, String> mapParamCodes() {
		Map<String, String> map = new HashMap<>();
		map.put("A+", "1040");
		map.put("A-", "1041");
		map.put("R+", "1042");
		map.put("R-", "1043");
		map.put("AB+", "1498");
		map.put("AB-", "1499");
		map.put("RB+", "1500");
		map.put("RB-", "1501");
		return map;
	}
}
