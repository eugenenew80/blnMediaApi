package kz.kegoc.bln.ejb.producer;

import javax.enterprise.inject.Produces;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import kz.kegoc.bln.ejb.annotation.DayData;
import kz.kegoc.bln.ejb.annotation.HourData;
import kz.kegoc.bln.ejb.annotation.MonthData;
import kz.kegoc.bln.ejb.annotation.ParamCodes;
import kz.kegoc.bln.ejb.annotation.EmcosParamUnits;
import kz.kegoc.bln.entity.media.day.DayMeteringBalanceRaw;
import kz.kegoc.bln.entity.media.day.DayMeteringDataRaw;
import kz.kegoc.bln.entity.media.hour.HourMeteringDataRaw;
import kz.kegoc.bln.entity.media.month.MonthMeteringDataRaw;
import kz.kegoc.bln.producer.emcos.reader.helper.EmcosConfig;
import kz.kegoc.bln.producer.emcos.reader.helper.EmcosPointCfg;

import org.dozer.DozerBeanMapper;
import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@ApplicationScoped
public class Producer {
	private RedissonClient redissonClient = null;
	private RBlockingQueue<HourMeteringDataRaw> hourMeteringDataQueue = null;
	private RBlockingQueue<DayMeteringDataRaw> dayMeteringDataQueue  = null;
	private RBlockingQueue<MonthMeteringDataRaw> monthMeteringDataQueue  = null;
	private RBlockingQueue<DayMeteringBalanceRaw> dayMeteringBalanceQueue  = null;
	private RList<EmcosPointCfg> emcosPointCfgList  = null;


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
	public RList<EmcosPointCfg> createEmcosPointCfgList() {
		if (emcosPointCfgList!=null)
			return emcosPointCfgList;

		createRedissonClient();
		emcosPointCfgList = redissonClient.getList("emcosPointCfgList");
		return emcosPointCfgList;
	}


	@Produces
	@ParamCodes
	public BiMap<String, String> mapParamCodes() {
		BiMap<String, String> map = HashBiMap.create();
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


	@Produces
	@EmcosParamUnits
	public Map<String, String> mapEmcosParamUnits() {
		Map<String, String> map = new HashMap<>();
		map.put("1040", "kWh");
		map.put("1041", "kWh");
		map.put("1042", "kVarh");
		map.put("1043", "kVarh");
		map.put("1498", "kWh");
		map.put("1499", "kWh");
		map.put("1500", "kVarh");
		map.put("1501", "kVarh");
		return map;
	}
	
	
	@Produces
	public EmcosConfig defaultEmcosConfig() {
		return EmcosConfig.defaultEmcosServer().build();
	}


	@Produces
	@DayData
	public DozerBeanMapper dayMeteringDataMapper() {
		DozerBeanMapper mapper = new DozerBeanMapper();
		mapper.setMappingFiles(Arrays.asList("mapping/raw/DayMeteringDataRawDtoDefaultMapping.xml"));
		return mapper;
	}

	@Produces
	@HourData
	public DozerBeanMapper hourMeteringDataMapper() {
		DozerBeanMapper mapper = new DozerBeanMapper();
		mapper.setMappingFiles(Arrays.asList("mapping/raw/HourMeteringDataRawDtoDefaultMapping.xml"));
		return mapper;
	}

	@Produces
	@MonthData
	public DozerBeanMapper monthMeteringDataMapper() {
		DozerBeanMapper mapper = new DozerBeanMapper();
		mapper.setMappingFiles(Arrays.asList("mapping/raw/MonthMeteringDataRawDtoDefaultMapping.xml"));
		return mapper;
	}
}
