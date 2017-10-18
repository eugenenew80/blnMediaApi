package kz.kegoc.bln.producer.emcos.hour;

import java.time.*;
import java.util.*;
import javax.ejb.*;
import javax.ejb.Singleton;
import javax.inject.*;

import kz.kegoc.bln.entity.media.DayMeteringBalanceRaw;
import kz.kegoc.bln.entity.media.HourMeteringDataRaw;
import kz.kegoc.bln.entity.media.MinuteMeteringDataRaw;
import kz.kegoc.bln.interceptor.ProducerMonitor;
import kz.kegoc.bln.producer.emcos.helper.EmcosConfig;
import kz.kegoc.bln.service.media.LoadMeteringInfoService;
import org.apache.commons.lang3.tuple.Pair;
import kz.kegoc.bln.producer.emcos.helper.impl.EmcosDataServiceImpl;
import kz.kegoc.bln.producer.emcos.helper.RegistryTemplate;
import kz.kegoc.bln.producer.MeteringDataProducer;
import kz.kegoc.bln.queue.MeteringDataQueue;
import kz.kegoc.bln.service.dict.MeteringPointService;

import static java.util.stream.Collectors.groupingBy;


@Singleton
@Startup
public class EmcosHourMeteringDataRawProducer implements MeteringDataProducer {

	@ProducerMonitor
	@Schedule(minute = "*/1", hour = "*", persistent = false)
	public void execute() {
		LocalDateTime requestedDateTime = buildRequestedDateTime();

		EmcosDataServiceImpl.Builder builder = new EmcosDataServiceImpl.Builder()
			.config(EmcosConfig.defaultEmcosServer().build())
			.points(meteringPointService.findAll())
			.reqestedDateTime(requestedDateTime)
			.registryTemplate(registryTemplate);

		Arrays.asList("AB+", "AB-", "RB+", "RB-").forEach(paramCode -> {
			try {
				List<DayMeteringBalanceRaw> meteringBalance = builder
					.paramCode(paramCode)
					.build()		
					.requestMeteringBalance();
				
				meteringBalance.stream().forEach(System.out::println);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		
		/*
		Arrays.asList("A+", "A-", "R+", "R-").forEach(paramCode -> {
			try {			
				List<MinuteMeteringDataRaw> meteringData = builder
					.paramCode(paramCode)
					.build()
					.requestMeteringData();
				
				queueService.addAll(buildHourMeteringData(meteringData));
				loadMeteringInfoService.updateLoadMeteringInfo(meteringData, requestedDateTime);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		});
		*/
    }


	private List<HourMeteringDataRaw> buildHourMeteringData(List<MinuteMeteringDataRaw> minuteMeteringData) {
		Map<Pair<String, LocalDate>, List<MinuteMeteringDataRaw>> mapDayMeteringData = minuteMeteringData
			.stream()
			.collect(groupingBy(m -> Pair.of(m.getExternalCode(), m.getMeteringDate().toLocalDate())));
			
		List<HourMeteringDataRaw> hourMeteringData = new ArrayList<>();
		for (Pair<String, LocalDate> pair : mapDayMeteringData.keySet()) {

			Map<Integer, List<MinuteMeteringDataRaw>> mapHourMeteringData =  mapDayMeteringData
				.get(pair)
				.stream()
				.collect(groupingBy(m -> m.getMeteringDate().getHour() ));
			
			for (Integer hour : mapHourMeteringData.keySet()) {
				HourMeteringDataRaw h = new HourMeteringDataRaw();
				h.setExternalCode(pair.getLeft());
				h.setMeteringDate(pair.getRight());
				h.setHour(hour);
				
				h.setStatus( mapHourMeteringData.get(hour).get(0).getStatus() );
				h.setDataSourceCode( mapHourMeteringData.get(hour).get(0).getDataSourceCode() );
				h.setParamCode( mapHourMeteringData.get(hour).get(0).getParamCode() );
				h.setUnitCode( mapHourMeteringData.get(hour).get(0).getUnitCode() );
				h.setWayEntering( mapHourMeteringData.get(hour).get(0).getWayEntering() );
				h.setVal(mapHourMeteringData.get(hour).stream().mapToDouble(m -> m.getVal()).sum());
				hourMeteringData.add(h);
			}
		}			
		
		return hourMeteringData;
	}
	
	
	private LocalDateTime buildRequestedDateTime() {
		LocalDateTime now = LocalDateTime.now().plusHours(1);
		return LocalDateTime.of(
					now.getYear(),
					now.getMonth(),
					now.getDayOfMonth(),
					now.getHour(),
					45
				);
	}

	@Inject
	private MeteringDataQueue<HourMeteringDataRaw> queueService;

	@Inject
	private LoadMeteringInfoService loadMeteringInfoService;

	@Inject
	private MeteringPointService meteringPointService;
	
	@Inject
	private RegistryTemplate registryTemplate;
}
