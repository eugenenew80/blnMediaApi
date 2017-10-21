package kz.kegoc.bln.producer.emcos.hour;

import java.time.*;
import java.util.*;
import javax.ejb.*;
import javax.ejb.Singleton;
import javax.inject.*;

import kz.kegoc.bln.entity.media.hour.HourMeteringDataRaw;
import kz.kegoc.bln.ejb.interceptor.ProducerMonitor;
import kz.kegoc.bln.producer.emcos.helper.*;
import kz.kegoc.bln.service.media.LastLoadInfoService;
import org.apache.commons.lang3.tuple.Pair;
import kz.kegoc.bln.producer.MeteringDataProducer;
import kz.kegoc.bln.queue.MeteringDataQueue;
import static java.util.stream.Collectors.groupingBy;


@Singleton
@Startup
public class EmcosHourMeteringDataRawProducer implements MeteringDataProducer {

	@ProducerMonitor
	@Schedule(minute = "*/5", hour = "*", persistent = false)
	public void execute() {
		LocalDateTime requestedTime = buildRequestedTime();

		Arrays.asList("A+", "A-", "R+", "R-").forEach(paramCode -> {
			List<MinuteMeteringDataDto> data = emcosDataService.request(paramCode, requestedTime);
			queueService.addAll(buildHourMeteringData(data));
			lastLoadInfoService.updateLastDataLoadDate(data);
		});
    }

	private List<HourMeteringDataRaw> buildHourMeteringData(List<MinuteMeteringDataDto> minuteMeteringData) {
		Map<Pair<String, LocalDate>, List<MinuteMeteringDataDto>> mapDayMeteringData = minuteMeteringData
			.stream()
			.collect(groupingBy(m -> Pair.of(m.getExternalCode(), m.getMeteringDate().toLocalDate())));
			
		List<HourMeteringDataRaw> hourMeteringData = new ArrayList<>();
		for (Pair<String, LocalDate> pair : mapDayMeteringData.keySet()) {

			Map<Integer, List<MinuteMeteringDataDto>> mapHourMeteringData =  mapDayMeteringData
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

	private LocalDateTime buildRequestedTime() {
		LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC+1"));
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
	private LastLoadInfoService lastLoadInfoService;
	
	@Inject
	private EmcosDataService emcosDataService;
}
