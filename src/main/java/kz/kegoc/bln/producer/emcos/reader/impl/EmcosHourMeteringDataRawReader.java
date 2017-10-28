package kz.kegoc.bln.producer.emcos.reader.impl;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import javax.ejb.*;
import javax.inject.Inject;
import com.google.common.collect.BiMap;
import kz.kegoc.bln.ejb.cdi.annotation.ParamCodes;
import kz.kegoc.bln.entity.media.raw.HourMeteringDataRaw;
import kz.kegoc.bln.gateway.emcos.EmcosCfgGateway;
import kz.kegoc.bln.gateway.emcos.EmcosDataGateway;
import kz.kegoc.bln.gateway.emcos.EmcosPointCfg;
import kz.kegoc.bln.gateway.emcos.MinuteMeteringData;
import kz.kegoc.bln.producer.emcos.reader.EmcosMeteringReader;
import kz.kegoc.bln.service.media.raw.LastLoadInfoService;
import org.apache.commons.lang3.tuple.Pair;
import kz.kegoc.bln.queue.MeteringDataQueue;
import static java.util.stream.Collectors.groupingBy;

@Stateless
public class EmcosHourMeteringDataRawReader implements EmcosMeteringReader<HourMeteringDataRaw> {

	public void read() {
		LocalDateTime requestedTime = buildRequestedTime();
		List<EmcosPointCfg> pointsCfg = emcosCfgGateway.request();

		paramCodes.keySet().stream()
			.filter( p -> !p.contains("B") ).forEach(p -> {
				List<MinuteMeteringData> data = emcosDataGateway
					.cfg(pointsCfg)
					.paramCode(p)
					.requestedTime(requestedTime)
					.request();

				queueService.addAll(buildHourMeteringData(data));
				lastLoadInfoService.updateLastDataLoadDate(data);
			});
    }

	private List<HourMeteringDataRaw> buildHourMeteringData(List<MinuteMeteringData> minuteMeteringData) {
		Map<Pair<String, LocalDate>, List<MinuteMeteringData>> mapDayMeteringData = minuteMeteringData
			.stream()
			.collect(groupingBy(m -> Pair.of(m.getExternalCode(), m.getMeteringDate().toLocalDate())));
			
		List<HourMeteringDataRaw> hourMeteringData = new ArrayList<>();
		for (Pair<String, LocalDate> pair : mapDayMeteringData.keySet()) {

			Map<Integer, List<MinuteMeteringData>> mapHourMeteringData =  mapDayMeteringData
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
		return LocalDateTime.now(ZoneId.of("UTC+1"))
			.minusMinutes(15)
			.truncatedTo(ChronoUnit.HOURS);
	}

	
	@Inject
	private MeteringDataQueue<HourMeteringDataRaw> queueService;

	@Inject
	private LastLoadInfoService lastLoadInfoService;

	@Inject
	EmcosCfgGateway emcosCfgGateway;

	@Inject
	private EmcosDataGateway emcosDataGateway;

	@Inject @ParamCodes
	private BiMap<String, String> paramCodes;
}
