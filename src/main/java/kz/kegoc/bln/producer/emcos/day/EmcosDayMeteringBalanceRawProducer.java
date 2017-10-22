package kz.kegoc.bln.producer.emcos.day;

import java.time.*;
import java.util.*;
import javax.ejb.*;
import javax.ejb.Singleton;
import javax.inject.*;

import com.google.common.collect.BiMap;
import kz.kegoc.bln.ejb.annotation.ParamCodes;
import kz.kegoc.bln.entity.media.day.DayMeteringBalanceRaw;
import kz.kegoc.bln.ejb.interceptor.ProducerMonitor;
import kz.kegoc.bln.producer.emcos.helper.EmcosBalanceService;
import kz.kegoc.bln.producer.MeteringDataProducer;
import kz.kegoc.bln.queue.MeteringDataQueue;
import kz.kegoc.bln.service.media.LastLoadInfoService;

@Singleton
@Startup
public class EmcosDayMeteringBalanceRawProducer implements MeteringDataProducer {

	@ProducerMonitor
	@Schedule(minute = "*/15", hour = "*", persistent = false)
	public void execute() {
		LocalDateTime requestedDateTime = buildRequestedDateTime();

		paramCodes.keySet()
			.stream()
			.filter( p -> p.contains("B") )
			.forEach(p -> {
				List<DayMeteringBalanceRaw> meteringBalance = emcosBalanceService.request(p, requestedDateTime);
				queueService.addAll(meteringBalance);
				lastLoadInfoService.updateLastBalanceLoadDate(meteringBalance);
			});
    }

	private LocalDateTime buildRequestedDateTime() {
		LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC+1")).plusDays(1);
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

	@Inject
	private LastLoadInfoService lastLoadInfoService;

	@Inject
	private EmcosBalanceService emcosBalanceService;

	@Inject @ParamCodes
	private BiMap<String, String> paramCodes;
}
