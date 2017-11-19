package kz.kegoc.bln.producer.emcos.reader.impl;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import javax.ejb.*;
import javax.inject.Inject;
import com.google.common.collect.BiMap;
import kz.kegoc.bln.ejb.cdi.annotation.ParamCodes;
import kz.kegoc.bln.entity.media.data.HourMeteringFlow;
import kz.kegoc.bln.gateway.emcos.EmcosCfgGateway;
import kz.kegoc.bln.gateway.emcos.EmcosFlowGateway;
import kz.kegoc.bln.gateway.emcos.EmcosPointCfg;
import kz.kegoc.bln.gateway.emcos.MinuteMeteringFlow;
import kz.kegoc.bln.producer.emcos.reader.EmcosMeteringDataReader;
import kz.kegoc.bln.service.media.data.LastLoadInfoService;
import org.apache.commons.lang3.tuple.Pair;
import kz.kegoc.bln.queue.MeteringDataQueue;
import static java.util.stream.Collectors.groupingBy;

@Stateless
public class EmcosHourMeteringFlowReader implements EmcosMeteringDataReader<HourMeteringFlow> {

	public void read() {
		LocalDateTime requestedTime = buildRequestedTime();
		List<EmcosPointCfg> pointsCfg = emcosCfgGateway.request();

		paramCodes.keySet().stream()
			.filter( p -> !p.contains("B") ).forEach(p -> {
				List<MinuteMeteringFlow> data = emcosDataGateway
					.cfg(pointsCfg)
					.paramCode(p)
					.requestedTime(requestedTime)
					.request();

				if (data.size()>0) {
					queueService.addAll(buildHourMeteringData(data));
					lastLoadInfoService.updateLastDataLoadDate(data);
				}
			});
    }

	private List<HourMeteringFlow> buildHourMeteringData(List<MinuteMeteringFlow> minuteMeteringData) {
		Map<Pair<String, LocalDate>, List<MinuteMeteringFlow>> mapDayMeteringData = minuteMeteringData
			.stream()
			.collect(groupingBy(m -> Pair.of(m.getExternalCode(), m.getMeteringDate().toLocalDate())));
			
		List<HourMeteringFlow> hourMeteringData = new ArrayList<>();
		for (Pair<String, LocalDate> pair : mapDayMeteringData.keySet()) {

			Map<Integer, List<MinuteMeteringFlow>> mapHourMeteringData =  mapDayMeteringData
				.get(pair)
				.stream()
				.collect(groupingBy(m -> m.getMeteringDate().getHour() ));
			
			for (Integer hour : mapHourMeteringData.keySet()) {
				HourMeteringFlow h = new HourMeteringFlow();
				h.setExternalCode(pair.getLeft());
				h.setMeteringDate(pair.getRight());
				h.setHour(hour);
				
				h.setStatus( mapHourMeteringData.get(hour).get(0).getStatus() );
				h.setDataSource( mapHourMeteringData.get(hour).get(0).getDataSource() );
				h.setParamCode( mapHourMeteringData.get(hour).get(0).getParamCode() );
				h.setUnitCode( mapHourMeteringData.get(hour).get(0).getUnitCode() );
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
	private MeteringDataQueue<HourMeteringFlow> queueService;

	@Inject
	private LastLoadInfoService lastLoadInfoService;

	@Inject
	EmcosCfgGateway emcosCfgGateway;

	@Inject
	private EmcosFlowGateway emcosDataGateway;

	@Inject @ParamCodes
	private BiMap<String, String> paramCodes;
}
