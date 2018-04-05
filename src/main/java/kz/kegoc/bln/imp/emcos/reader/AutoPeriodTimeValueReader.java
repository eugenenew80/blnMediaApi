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
import kz.kegoc.bln.gateway.emcos.*;
import kz.kegoc.bln.imp.raw.PeriodTimeValueRaw;
import kz.kegoc.bln.imp.BatchHelper;
import kz.kegoc.bln.imp.Reader;
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
public class AutoPeriodTimeValueReader implements Reader<PeriodTimeValueRaw> {
	private static final Logger logger = LoggerFactory.getLogger(AutoPeriodTimeValueReader.class);

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

					Batch batch = batchHelper.createBatch(new Batch(header, ParamTypeEnum.PT));
					Long recCount = 0l;
					try {
						for (int i = 0; i < groupsPoints.size(); i++) {
							logger.info("group of points: " + (i + 1));

							List<PeriodTimeValueRaw> ptList = ptGateway
								.config(header.getConfig())
								.points(groupsPoints.get(i))
								.request();

							batchHelper.savePtData(batch, ptList);
							recCount = recCount + ptList.size();
						}
					}
					catch (Exception e) {
						logger.error("read failed: " + e.getMessage());
						batchHelper.updateBatch(batch, e, null);
						break;
					}

					batchHelper.updateBatch(batch, null, recCount);
					onBatchCompleted(batch);

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
			.collect(groupingBy(index -> index / 1200))
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
		lastLoadInfoService.ptUpdateLastDate(batch.getId());
		lastLoadInfoService.ptLoad(batch.getId());
		logger.info("onBatchCompleted completed");
	}

	private List<MeteringPointCfg> buildPoints(List<WorkListLine> lines, LocalDateTime endDateTime) {
		List<LastLoadInfo> lastLoadInfoList = lastLoadInfoService.findAll();

		List<MeteringPointCfg> points = new ArrayList<>();
		lines.stream()
			.filter(line -> line.getParam().getParamType().equals(newInstance(ParamTypeEnum.PT)))
			.forEach(line -> {
				LastLoadInfo lastLoadInfo = batchHelper.getLastLoadIfo(lastLoadInfoList, line);
				MeteringPointCfg mpc = batchHelper.buildPointCfg(line, buildStartTime(lastLoadInfo), endDateTime);
				if (mpc!=null && mpc.getStartTime().isBefore(mpc.getEndTime()))
					points.add(mpc);
			});

		return points;
	}

	private LocalDateTime buildStartTime(LastLoadInfo lastLoadInfo) {
		LocalDateTime startTime = LocalDate.now(ZoneId.of("UTC+1")).minusDays(3).atStartOfDay();
		if (lastLoadInfo!=null && lastLoadInfo.getLastLoadDate() !=null) {
			LocalDateTime lastLoadDate = lastLoadInfo.getLastLoadDate();
			startTime = lastLoadDate.getMinute() < 45
				? lastLoadDate.truncatedTo(ChronoUnit.HOURS)
				: lastLoadDate.plusMinutes(15);
		}

		return startTime;
	}

	private LocalDateTime buildEndDateTime() {
		return LocalDateTime.now(ZoneId.of("UTC+1")).minusMinutes(15).truncatedTo(ChronoUnit.HOURS);
	}


	@Inject
	private LastLoadInfoService lastLoadInfoService;

	@Inject
	private PeriodTimeValueImpGateway ptGateway;

	@Inject
	private WorkListHeaderService workListHeaderService;

	@Inject
	private BatchHelper batchHelper;
}