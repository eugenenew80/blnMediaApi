package kz.kegoc.bln.producer.emcos.reader.impl;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import javax.ejb.*;
import javax.inject.Inject;
import com.google.common.collect.BiMap;
import kz.kegoc.bln.ejb.cdi.annotation.ParamCodes;
import kz.kegoc.bln.entity.data.MeasData;
import kz.kegoc.bln.gateway.emcos.EmcosCfgGateway;
import kz.kegoc.bln.gateway.emcos.EmcosFlowGateway;
import kz.kegoc.bln.gateway.emcos.EmcosPointCfg;
import kz.kegoc.bln.entity.data.MeasDataRaw;
import kz.kegoc.bln.producer.emcos.reader.EmcosMeteringDataReader;
import kz.kegoc.bln.service.data.LastLoadInfoService;
import org.apache.commons.lang3.tuple.Pair;
import kz.kegoc.bln.queue.MeteringDataQueue;
import static java.util.stream.Collectors.groupingBy;

@Stateless
public class MeasDataReader implements EmcosMeteringDataReader<MeasData> {

	public void read() {
		LocalDateTime requestedTime = buildRequestedTime();
		List<EmcosPointCfg> pointsCfg = emcosCfgGateway.request();

		paramCodes.keySet().stream()
			.filter( p -> !p.contains("B") ).forEach(p -> {
				List<MeasDataRaw> data = emcosDataGateway
					.cfg(pointsCfg)
					.paramCode(p)
					.requestedTime(requestedTime)
					.request();

				if (data.size()>0) {
					//queueService.addAll(buildHourMeteringData(data));
					queueService.addAll(data);
					lastLoadInfoService.updateLastDataLoadDate(data);
				}
			});
    }

    /*
	private List<MeasData> buildHourMeteringData(List<MeasDataRaw> minuteMeteringData) {
		Map<Pair<String, LocalDate>, List<MeasDataRaw>> mapDayMeteringData = minuteMeteringData
			.stream()
			.collect(groupingBy(m -> Pair.of(m.getSourceMeteringPointCode(), m.getMeteringDate().toLocalDate())));
			
		List<MeasData> hourMeteringData = new ArrayList<>();
		for (Pair<String, LocalDate> pair : mapDayMeteringData.keySet()) {

			Map<Integer, List<MeasDataRaw>> mapHourMeteringData =  mapDayMeteringData
				.get(pair)
				.stream()
				.collect(groupingBy(m -> m.getMeteringDate().getHour() ));
			
			for (Integer hour : mapHourMeteringData.keySet()) {
				MeasData h = new MeasData();
				h.setSourceMeteringPointCode(pair.getLeft());
				h.setMeasDate( pair.getRight().atStartOfDay().plusHours(hour) );
				h.setIntervalType(mapHourMeteringData.get(hour).get(0).getIntervalType());
				h.setStatus( mapHourMeteringData.get(hour).get(0).getStatus() );
				h.setDataSourceCode( mapHourMeteringData.get(hour).get(0).getDataSourceCode() );
				h.setParamCode( mapHourMeteringData.get(hour).get(0).getParamCode() );
				h.setSourceParamCode(mapHourMeteringData.get(hour).get(0).getSourceParamCode());
				h.setSourceUnitCode( mapHourMeteringData.get(hour).get(0).getSourceUnitCode() );
				h.setVal(mapHourMeteringData.get(hour).stream().mapToDouble(m -> m.getVal()).sum());
				hourMeteringData.add(h);
			}
		}			
		
		return hourMeteringData;
	}
	*/

	private LocalDateTime buildRequestedTime() {
		return LocalDateTime.now(ZoneId.of("UTC+1"))
			.minusMinutes(15)
			.truncatedTo(ChronoUnit.HOURS);
	}

	
	@Inject
	private MeteringDataQueue<MeasDataRaw> queueService;

	@Inject
	private LastLoadInfoService lastLoadInfoService;

	@Inject
	private EmcosCfgGateway emcosCfgGateway;

	@Inject
	private EmcosFlowGateway emcosDataGateway;

	@Inject @ParamCodes
	private BiMap<String, String> paramCodes;
}
