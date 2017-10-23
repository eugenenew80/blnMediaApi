package kz.kegoc.bln.producer.emcos.reader.impl.day;

import java.time.*;
import java.util.*;
import javax.ejb.*;
import javax.inject.*;
import com.google.common.collect.BiMap;
import kz.kegoc.bln.ejb.annotation.ParamCodes;
import kz.kegoc.bln.entity.media.day.DayMeteringBalanceRaw;
import kz.kegoc.bln.producer.emcos.helper.EmcosCfgService;
import kz.kegoc.bln.producer.emcos.reader.EmcosMeteringDataReader;
import kz.kegoc.bln.producer.emcos.helper.EmcosBalanceService;
import kz.kegoc.bln.queue.MeteringDataQueue;
import kz.kegoc.bln.service.media.LastLoadInfoService;

@Stateless
public class EmcosDayMeteringBalanceRawProducer implements EmcosMeteringDataReader<DayMeteringBalanceRaw> {

	public void loadFromEmcos() {
		LocalDateTime requestedDateTime = buildRequestedDateTime();
		emcosBalanceService.setPointsCfg(new ArrayList<>(emcosCfgService.request()));

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
	EmcosCfgService emcosCfgService;

	@Inject
	private EmcosBalanceService emcosBalanceService;

	@Inject @ParamCodes
	private BiMap<String, String> paramCodes;
}
