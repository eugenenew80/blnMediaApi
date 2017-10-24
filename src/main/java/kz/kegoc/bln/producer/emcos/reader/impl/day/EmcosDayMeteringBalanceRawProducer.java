package kz.kegoc.bln.producer.emcos.reader.impl.day;

import java.time.*;
import java.util.*;
import javax.ejb.*;
import javax.inject.Inject;
import com.google.common.collect.BiMap;
import kz.kegoc.bln.ejb.annotation.ParamCodes;
import kz.kegoc.bln.entity.media.day.DayMeteringBalanceRaw;
import kz.kegoc.bln.producer.emcos.gateway.*;
import kz.kegoc.bln.producer.emcos.reader.EmcosMeteringDataReader;
import kz.kegoc.bln.queue.MeteringDataQueue;
import kz.kegoc.bln.service.media.LastLoadInfoService;

@Stateless
public class EmcosDayMeteringBalanceRawProducer implements EmcosMeteringDataReader<DayMeteringBalanceRaw> {

	public void loadFromEmcos() {
		LocalDateTime requestedTime = buildRequestedDateTime();
		List<EmcosPointCfg> pointsCfg = emcosCfgService.request();

		paramCodes.keySet().stream()
			.filter( p -> p.contains("B") ).forEach(p -> {
				List<DayMeteringBalanceRaw> meteringBalance = emcosBalanceService
					.cfg(pointsCfg)
					.paramCode(p)
					.requestedTime(requestedTime)
					.request();

				queueService.addAll(meteringBalance);
				lastLoadInfoService.updateLastBalanceLoadDate(meteringBalance);
			});
    }

	private LocalDateTime buildRequestedDateTime() {
		return LocalDate.now(ZoneId.of("UTC+1"))
			.plusDays(1)
			.atStartOfDay();
	}

	@Inject
	private MeteringDataQueue<DayMeteringBalanceRaw> queueService;

	@Inject
	private LastLoadInfoService lastLoadInfoService;

	@Inject
	EmcosCfgGateway emcosCfgService;

	@Inject
	private EmcosBalanceGateway emcosBalanceService;

	@Inject @ParamCodes
	private BiMap<String, String> paramCodes;
}
