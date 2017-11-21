package kz.kegoc.bln.ejb.cdi;

import javax.enterprise.inject.Produces;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import kz.kegoc.bln.ejb.cdi.annotation.*;
import kz.kegoc.bln.entity.common.Lang;
import kz.kegoc.bln.entity.data.DayMeteringBalance;
import kz.kegoc.bln.entity.data.DayMeteringFlow;
import kz.kegoc.bln.entity.data.HourMeteringFlow;
import kz.kegoc.bln.entity.data.MonthMeteringFlow;
import kz.kegoc.bln.gateway.emcos.EmcosConfig;
import kz.kegoc.bln.gateway.emcos.EmcosPointCfg;

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
	private RBlockingQueue<HourMeteringFlow> hourMeteringFlowQueue = null;
	private RBlockingQueue<DayMeteringFlow> dayMeteringFlowQueue = null;
	private RBlockingQueue<MonthMeteringFlow> monthMeteringFlowQueue = null;
	private RBlockingQueue<DayMeteringBalance> dayMeteringBalanceQueue  = null;
	private RList<EmcosPointCfg> emcosPointCfgList  = null;
	private DozerBeanMapper mapper = null;

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
	public RBlockingQueue<HourMeteringFlow> createHourMeteringDataQueue() {
		if (hourMeteringFlowQueue !=null)
			return hourMeteringFlowQueue;

		createRedissonClient();
		hourMeteringFlowQueue = redissonClient.getBlockingQueue("hourMeteringFlow");
		return hourMeteringFlowQueue;
	}


	@Produces
	public RBlockingQueue<DayMeteringFlow> createDayMeteringDataQueue() {
		if (dayMeteringFlowQueue !=null)
			return dayMeteringFlowQueue;

		createRedissonClient();
		dayMeteringFlowQueue = redissonClient.getBlockingQueue("dayMeteringFlow");
		return dayMeteringFlowQueue;
	}


	@Produces
	public RBlockingQueue<MonthMeteringFlow> createMonthMeteringDataQueue() {
		if (monthMeteringFlowQueue !=null)
			return monthMeteringFlowQueue;

		createRedissonClient();
		monthMeteringFlowQueue = redissonClient.getBlockingQueue("monthMeteringFlow");
		return monthMeteringFlowQueue;
	}


	@Produces
	public RBlockingQueue<DayMeteringBalance> createDayMeteringBalanceQueue() {
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
	@MeteringDataPath
	public String meteringDataPath() {
		return "/home/eugene/dev/src/IdeaProjects/data";
	}

	@Produces
	public EmcosConfig defaultEmcosConfig() {
		return EmcosConfig.defaultEmcosServer().build();
	}


	@Produces
	public DozerBeanMapper dozerBeanMapper() {
		if (mapper!=null)
			return mapper;
		
		mapper = new DozerBeanMapper();
		mapper.setMappingFiles(Arrays.asList(
			"mapping/MappingConfig.xml",
				"mapping/doc/default/GroupDto.xml",
				"mapping/doc/default/GroupMeteringPointDto.xml",
				"mapping/doc/default/DocTypeDto.xml",
				"mapping/doc/default/DocMeteringReadingHeaderDto.xml",
				"mapping/doc/default/DocMeteringReadingLineDto.xml",
				"mapping/doc/default/DocMeterReplacingHeaderDto.xml",
				"mapping/doc/default/DocMeterReplacingLineDto.xml",
				"mapping/doc/default/DocUnderAccountingHeaderDto.xml",
				"mapping/doc/default/DocUnderAccountingMeasLineDto.xml",
				"mapping/doc/default/DocUnderAccountingCalcLineDto.xml"
		));
		return mapper;
	}


	@Produces
	public Lang defLang() {
		return Lang.RU;
	}
}
