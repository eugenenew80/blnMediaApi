package kz.kegoc.bln.producer.emcos.reader.impl;

import java.time.*;
import java.util.*;
import javax.ejb.*;
import javax.inject.Inject;
import com.google.common.collect.BiMap;
import kz.kegoc.bln.ejb.cdi.annotation.ParamCodes;
import kz.kegoc.bln.entity.data.MeteringReadingRaw;
import kz.kegoc.bln.gateway.emcos.EmcosBalanceGateway;
import kz.kegoc.bln.gateway.emcos.EmcosCfgGateway;
import kz.kegoc.bln.gateway.emcos.EmcosPointCfg;
import kz.kegoc.bln.producer.emcos.reader.EmcosMeteringDataReader;
import kz.kegoc.bln.queue.MeteringDataQueue;
import kz.kegoc.bln.service.data.LastLoadInfoService;

@Stateless
public class MeteringReadingReader implements EmcosMeteringDataReader<MeteringReadingRaw> {

	public void read() {
		LocalDateTime requestedTime = buildRequestedDateTime();
		List<EmcosPointCfg> pointsCfg = emcosCfgGateway.request();

		paramCodes.keySet().stream()
			.filter( p -> p.contains("B") ).forEach(p -> {
				List<MeteringReadingRaw> meteringBalance = emcosBalanceGateway
					.cfg(pointsCfg)
					.paramCode(p)
					.requestedTime(requestedTime)
					.request();

				if (meteringBalance.size()>0) {
					queueService.addAll(meteringBalance);
					lastLoadInfoService.updateLastBalanceLoadDate(meteringBalance);
				}
			});
    }

	private LocalDateTime buildRequestedDateTime() {
		return LocalDate.now(ZoneId.of("UTC+1"))
			.plusDays(1)
			.atStartOfDay();
	}

	@Inject
	private MeteringDataQueue<MeteringReadingRaw> queueService;

	@Inject
	private LastLoadInfoService lastLoadInfoService;

	@Inject
	private EmcosCfgGateway emcosCfgGateway;

	@Inject
	private EmcosBalanceGateway emcosBalanceGateway;

	@Inject @ParamCodes
	private BiMap<String, String> paramCodes;
}
