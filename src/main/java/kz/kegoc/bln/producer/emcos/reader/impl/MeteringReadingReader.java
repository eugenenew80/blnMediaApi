package kz.kegoc.bln.producer.emcos.reader.impl;

import java.time.*;
import java.util.*;
import javax.ejb.*;
import javax.inject.Inject;
import com.google.common.collect.BiMap;
import kz.kegoc.bln.ejb.cdi.annotation.ParamCodes;
import kz.kegoc.bln.entity.data.EmcosConfig;
import kz.kegoc.bln.entity.data.MeteringReadingRaw;
import kz.kegoc.bln.entity.data.WorkListHeader;
import kz.kegoc.bln.gateway.emcos.MeteringReadingRawGateway;
import kz.kegoc.bln.gateway.emcos.MeteringPointCfgGateway;
import kz.kegoc.bln.gateway.emcos.MeteringPointCfg;
import kz.kegoc.bln.producer.emcos.reader.EmcosMeteringDataReader;
import kz.kegoc.bln.queue.MeteringDataQueue;
import kz.kegoc.bln.service.data.EmcosConfigService;
import kz.kegoc.bln.service.data.LastLoadInfoService;
import kz.kegoc.bln.service.data.WorkListHeaderService;

@Stateless
public class MeteringReadingReader implements EmcosMeteringDataReader<MeteringReadingRaw> {

	public void read() {
		WorkListHeader header = headerService.findById(1L);
		System.out.println(header.getName());
		System.out.println(header.getConfig().getUrl());
		System.out.println(header.getLines().size());


		/*
		LocalDateTime requestedTime = buildRequestedDateTime();
		List<MeteringPointCfg> pointsCfg = emcosCfgGateway.request();

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
		*/
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
	private MeteringPointCfgGateway emcosCfgGateway;

	@Inject
	private MeteringReadingRawGateway emcosBalanceGateway;

	@Inject @ParamCodes
	private BiMap<String, String> paramCodes;

	@Inject
	private WorkListHeaderService headerService;
}
