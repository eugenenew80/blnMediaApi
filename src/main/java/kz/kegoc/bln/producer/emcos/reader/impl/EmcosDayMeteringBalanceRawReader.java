package kz.kegoc.bln.producer.emcos.reader.impl;

import java.time.*;
import java.util.*;
import javax.ejb.*;
import javax.inject.Inject;
import com.google.common.collect.BiMap;
import kz.kegoc.bln.ejb.cdi.annotation.ParamCodes;
import kz.kegoc.bln.entity.media.raw.DayMeteringBalanceRaw;
import kz.kegoc.bln.gateway.emcos.EmcosBalanceGateway;
import kz.kegoc.bln.gateway.emcos.EmcosCfgGateway;
import kz.kegoc.bln.gateway.emcos.EmcosPointCfg;
import kz.kegoc.bln.producer.emcos.reader.EmcosMeteringReader;
import kz.kegoc.bln.queue.MeteringDataQueue;
import kz.kegoc.bln.service.media.raw.LastLoadInfoService;

@Stateless
public class EmcosDayMeteringBalanceRawReader implements EmcosMeteringReader<DayMeteringBalanceRaw> {

	public void read() {
		LocalDateTime requestedTime = buildRequestedDateTime();
		List<EmcosPointCfg> pointsCfg = emcosCfgGateway.request();

		paramCodes.keySet().stream()
			.filter( p -> p.contains("B") ).forEach(p -> {
				List<DayMeteringBalanceRaw> meteringBalance = emcosBalanceGateway
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
	private EmcosCfgGateway emcosCfgGateway;

	@Inject
	private EmcosBalanceGateway emcosBalanceGateway;

	@Inject @ParamCodes
	private BiMap<String, String> paramCodes;
}
