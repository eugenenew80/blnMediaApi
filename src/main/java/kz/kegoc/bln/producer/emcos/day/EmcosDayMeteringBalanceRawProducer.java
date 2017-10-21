package kz.kegoc.bln.producer.emcos.day;

import java.time.*;
import java.util.*;
import javax.ejb.*;
import javax.ejb.Singleton;
import javax.inject.*;

import kz.kegoc.bln.entity.media.day.DayMeteringBalanceRaw;
import kz.kegoc.bln.interceptor.ProducerMonitor;
import kz.kegoc.bln.producer.emcos.helper.EmcosDataService;
import kz.kegoc.bln.producer.MeteringDataProducer;
import kz.kegoc.bln.queue.MeteringDataQueue;
import kz.kegoc.bln.service.media.LastLoadInfoService;


@Singleton
@Startup
public class EmcosDayMeteringBalanceRawProducer implements MeteringDataProducer {

	@ProducerMonitor
	@Schedule(minute = "*/5", hour = "*", persistent = false)
	public void execute() {
		LocalDateTime requestedDateTime = buildRequestedDateTime();

		Arrays.asList("AB+", "AB-", "RB+", "RB-").forEach(paramCode -> {
			List<DayMeteringBalanceRaw> meteringBalance = emcosDataService.requestBalance(paramCode, requestedDateTime);
			queueService.addAll(meteringBalance);
			lastLoadInfoService.updateLastBalanceLoadDate(meteringBalance);
		});
    }

	
	private LocalDateTime buildRequestedDateTime() {
		LocalDateTime now = LocalDateTime.now().plusHours(1).plusDays(1);
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
	private EmcosDataService emcosDataService;
}
