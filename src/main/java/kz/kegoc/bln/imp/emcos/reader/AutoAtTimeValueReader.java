package kz.kegoc.bln.imp.emcos.reader;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import javax.ejb.*;
import javax.inject.Inject;

import kz.kegoc.bln.ejb.annotation.Auto;
import kz.kegoc.bln.ejb.annotation.Emcos;
import kz.kegoc.bln.common.enums.DirectionEnum;
import kz.kegoc.bln.common.enums.ParamTypeEnum;
import kz.kegoc.bln.common.enums.SourceSystemEnum;
import kz.kegoc.bln.entity.media.*;
import kz.kegoc.bln.gateway.emcos.AtTimeValueGateway;
import kz.kegoc.bln.gateway.emcos.MeteringPointCfg;
import kz.kegoc.bln.imp.BatchHelper;
import kz.kegoc.bln.imp.Reader;
import kz.kegoc.bln.imp.raw.AtTimeValueRaw;
import kz.kegoc.bln.service.LastLoadInfoService;
import kz.kegoc.bln.service.WorkListHeaderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static kz.kegoc.bln.entity.media.ParamType.newInstance;

@Stateless
@Emcos @Auto
public class AutoAtTimeValueReader implements Reader<AtTimeValueRaw> {
	private static final Logger logger = LoggerFactory.getLogger(AutoAtTimeValueReader.class);

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void read() {
		logger.info("read started");

		workListHeaderService.findAll().stream()
			.filter(h -> h.getActive()
				&& SourceSystem.newInstance(SourceSystemEnum.EMCOS).equals(h.getSourceSystemCode())
				&& Direction.newInstance(DirectionEnum.IMPORT).equals(h.getDirection())
				&& StringUtils.equals(h.getWorkListType(), "SYS")
				&& h.getConfig()!=null
			)
			.forEach(header -> {
				logger.info("headerId: " + header.getId());
				logger.info("url: " + header.getConfig().getUrl());
				logger.info("user: " + header.getConfig().getUserName());

				LocalDateTime endDateTime = buildEndDateTime();
				List<MeteringPointCfg> points = buildPoints(header.getLines(), endDateTime);
				if (points.size()==0) {
					logger.info("List of points is empty, import data stopped");
					return;
				}

				LocalDateTime lastLoadDateTime = points.stream()
					.map(p -> p.getStartTime())
					.max(LocalDateTime::compareTo)
					.orElse(endDateTime);

				LocalDateTime requestedDateTime = lastLoadDateTime.plusDays(1);
				if (requestedDateTime.isAfter(endDateTime))
					requestedDateTime=endDateTime;

				while (!endDateTime.isBefore(requestedDateTime)) {
					logger.info("requested date: " + requestedDateTime);

					points = buildPoints(header.getLines(), requestedDateTime);
					final List<List<MeteringPointCfg>> groupsPoints = splitPoints(points);

					Batch batch = batchHelper.createBatch(new Batch(header, ParamTypeEnum.AT));
					Long recCount = 0l;
					try {
						for (int i = 0; i < groupsPoints.size(); i++) {
							logger.info("group of points: " + (i + 1));

							List<AtTimeValueRaw> atList = atGateway
								.config(header.getConfig())
								.points(groupsPoints.get(i))
								.request();

							batchHelper.saveAtData(batch, atList);
							recCount = recCount + atList.size();
						}

						batchHelper.updateBatch(batch, null, recCount);
						onBatchCompleted(batch);
					}
					catch (Exception e) {
						logger.error("read failed: " + e.getMessage());
						batchHelper.updateBatch(batch, e, null);
						break;
					}

					if (requestedDateTime.isEqual(endDateTime))
						break;

					requestedDateTime = requestedDateTime.plusDays(1);
					if (requestedDateTime.isAfter(endDateTime))
						requestedDateTime=endDateTime;
				}
			});

		logger.info("read completed");
    }

	private List<List<MeteringPointCfg>> splitPoints(List<MeteringPointCfg> points) {
		return range(0, points.size())
			.boxed()
			.collect(groupingBy(index -> index / 6000))
			.values()
			.stream()
			.map(indices -> indices
				.stream()
				.map(points::get)
				.collect(toList()))
			.collect(toList());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void onBatchCompleted(Batch batch) {
		logger.info("onBatchCompleted started");
		lastLoadInfoService.atUpdateLastDate(batch.getId());
		lastLoadInfoService.atLoad(batch.getId());
		logger.info("onBatchCompleted completed");
	}

	private List<MeteringPointCfg> buildPoints(List<WorkListLine> lines, LocalDateTime endDateTime) {
		List<LastLoadInfo> lastLoadInfoList = lastLoadInfoService.findAll();

		List<MeteringPointCfg> points = new ArrayList<>();
		lines.stream()
			.filter(line -> line.getParam().getIsAt())
			.forEach(line -> {
				LastLoadInfo lastLoadInfo = batchHelper.getLastLoadIfo(
					lastLoadInfoList,
					line,
					ParamTypeEnum.AT,
					null
				);

				MeteringPointCfg mpc = batchHelper.buildPointCfg(
					line,
					buildStartTime(lastLoadInfo),
					endDateTime,
					ParamTypeEnum.AT,
					null
				);
				if (mpc!=null) points.add(mpc);
			});

		return points;
	}

	private LocalDateTime buildStartTime(LastLoadInfo lastLoadInfo) {
		LocalDate now = LocalDate.now(ZoneId.of("UTC+1"));
		LocalDateTime startDateTime =  now
			.minusDays(now.getDayOfMonth())
			.minusMonths(1)
			.atStartOfDay();

		if (lastLoadInfo!=null && lastLoadInfo.getLastLoadDate()!=null)
			startDateTime = lastLoadInfo.getLastLoadDate().plusDays(1).truncatedTo(ChronoUnit.DAYS);

		return startDateTime;
	}

	private LocalDateTime buildEndDateTime() {
		return LocalDate.now(ZoneId.of("UTC+1"))
			.plusDays(1)
			.atStartOfDay();
	}


	@Inject
	private LastLoadInfoService lastLoadInfoService;

	@Inject
	private AtTimeValueGateway atGateway;

	@Inject
	private WorkListHeaderService workListHeaderService;

	@Inject
	private BatchHelper batchHelper;
}
