package kz.kegoc.bln.producer.emcos.day;

import java.time.*;
import java.util.*;
import javax.ejb.*;
import javax.ejb.Singleton;
import javax.inject.*;

import kz.kegoc.bln.entity.media.DayMeteringBalanceRaw;
import kz.kegoc.bln.entity.media.EmcosMeteringPointCfg;
import kz.kegoc.bln.interceptor.ProducerMonitor;
import kz.kegoc.bln.producer.emcos.helper.EmcosConfig;
//import kz.kegoc.bln.service.media.LoadMeteringInfoService;
import kz.kegoc.bln.producer.emcos.helper.impl.EmcosDataServiceImpl;
import kz.kegoc.bln.producer.emcos.helper.RegistryTemplate;
import kz.kegoc.bln.producer.MeteringDataProducer;
import kz.kegoc.bln.queue.MeteringDataQueue;
import kz.kegoc.bln.service.dict.MeteringPointService;


@Singleton
@Startup
public class EmcosDayMeteringBalanceRawProducer implements MeteringDataProducer {

	@ProducerMonitor
	@Schedule(minute = "*/5", hour = "*", persistent = false)
	public void execute() {
		
		List<EmcosMeteringPointCfg> pointsCfg = null;
		try {
			pointsCfg = new EmcosDataServiceImpl.Builder()
				.config(EmcosConfig.defaultEmcosServer().build())
				.registryTemplate(registryTemplate)
				.build()
				.requestCfg();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}		
		
		
		LocalDateTime requestedDate = buildRequestedDateTime();
		EmcosDataServiceImpl.Builder builder = new EmcosDataServiceImpl.Builder()
			.config(EmcosConfig.defaultEmcosServer().build())
			.points(meteringPointService.findAll())
			.reqestedDateTime(requestedDate)
			.registryTemplate(registryTemplate)
			.pointsCfg(pointsCfg);

		Arrays.asList("AB+", "AB-", "RB+", "RB-").forEach(paramCode -> {
			try {
				List<DayMeteringBalanceRaw> meteringBalance = builder
					.paramCode(paramCode)
					.build()		
					.requestMeteringBalance();
				
				queueService.addAll(meteringBalance);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		});
    }

	
	private LocalDateTime buildRequestedDateTime() {
		LocalDateTime now = LocalDateTime.now().plusHours(1).plusDays(1);
		return LocalDateTime.of(
					now.getYear(),
					now.getMonth(),
					now.getDayOfMonth(),
					0,
					0
				);
	}
	
	
	@Inject
	private MeteringDataQueue<DayMeteringBalanceRaw> queueService;

	//@Inject
	//private LoadMeteringInfoService loadMeteringInfoService;

	@Inject
	private MeteringPointService meteringPointService;
	
	@Inject
	private RegistryTemplate registryTemplate;
}
