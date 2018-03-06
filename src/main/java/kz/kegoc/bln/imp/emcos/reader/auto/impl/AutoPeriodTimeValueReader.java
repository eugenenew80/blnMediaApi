package kz.kegoc.bln.imp.emcos.reader.auto.impl;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import javax.ejb.*;
import javax.inject.Inject;
import kz.kegoc.bln.entity.common.DirectionEnum;
import kz.kegoc.bln.entity.common.ParamTypeEnum;
import kz.kegoc.bln.entity.common.SourceSystemEnum;
import kz.kegoc.bln.entity.data.*;
import kz.kegoc.bln.gateway.emcos.*;
import kz.kegoc.bln.entity.data.PeriodTimeValueRaw;
import kz.kegoc.bln.imp.emcos.reader.BatchHelper;
import kz.kegoc.bln.imp.emcos.reader.auto.AutoReader;
import kz.kegoc.bln.service.data.LastLoadInfoService;
import kz.kegoc.bln.service.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static kz.kegoc.bln.entity.data.ParamType.newInstance;

@Stateless
public class AutoPeriodTimeValueReader implements AutoReader<PeriodTimeValueRaw> {
	private static final Logger logger = LoggerFactory.getLogger(AutoPeriodTimeValueReader.class);

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void read() {
		logger.info("AutoPeriodTimeValueReader.read started");

		workListHeaderService.findAll().stream()
			.filter(h -> h.getActive()
				&& SourceSystem.newInstance(SourceSystemEnum.EMCOS).equals(h.getSourceSystemCode())
				&& Direction.newInstance(DirectionEnum.IMPORT).equals(h.getDirection())
				&& h.getConfig()!=null
			)
			.forEach(header -> {
				logger.info("headerId: " + header.getId());
				logger.info("url: " + header.getConfig().getUrl());
				logger.info("user: " + header.getConfig().getUserName());

				if (header.getLines().size()==0) {
					logger.info("List of points is empty, import data stopped");
					return;
				}

				List<MeteringPointCfg> points = buildPoints(header.getLines());
				if (points.size()==0) {
					logger.info("Import data is not required, import data stopped");
					return;
				}

				Batch batch = batchHelper.createBatch(new Batch(header, ParamTypeEnum.PT));

				final List<List<MeteringPointCfg>> groupsPoints = range(0, points.size())
					.boxed()
					.collect(groupingBy(index -> index / 400))
					.values()
					.stream()
					.map(indices -> indices
						.stream()
						.map(points::get)
						.collect(toList()))
					.collect(toList());

				Long recCount = 0l;
				try {
					for (int i = 0; i < groupsPoints.size(); i++) {
						List<MeteringPointCfg> groupPoints = groupsPoints.get(i);
						logger.info("group of points num: " + (i + 1));

						List<PeriodTimeValueRaw> ptList = ptGateway
							.config(header.getConfig())
							.points(groupPoints)
							.request();

						batchHelper.savePtData(batch, ptList);
						recCount = recCount + ptList.size();
					}

					lastLoadInfoService.pcUpdateLastDate(batch.getId());
					lastLoadInfoService.pcLoad(batch.getId());
					batchHelper.updateBatch(batch, null, recCount);
				}
				catch (Exception e) {
					logger.error("AutoPeriodTimeValueReader.read failed: " + e.getMessage());
					batchHelper.updateBatch(batch, e, null);
				}
			});

		logger.info("AutoPeriodTimeValueReader.read completed");
	}


	private List<MeteringPointCfg> buildPoints(List<WorkListLine> lines) {
		List<LastLoadInfo> lastLoadInfoList = lastLoadInfoService.findAll();

		List<MeteringPointCfg> points = new ArrayList<>();
		lines.stream()
			.filter(line -> line.getParam().getParamType().equals(newInstance(ParamTypeEnum.PT)))
			.forEach(line -> {
				LastLoadInfo lastLoadInfo = batchHelper.getLastLoadIfo(lastLoadInfoList, line);
				MeteringPointCfg mpc = batchHelper.buildPointCfg(line, buildStartTime(lastLoadInfo), buildRequestedDateTime());
				if (!(mpc.getStartTime().isEqual(mpc.getEndTime()) || mpc.getStartTime().isAfter(mpc.getEndTime())))
					points.add(mpc);
			});

		return points;
	}


	private LocalDateTime buildRequestedDateTime() {
		return LocalDateTime.now(ZoneId.of("UTC+1")).minusMinutes(15).truncatedTo(ChronoUnit.HOURS);
	}

	private LocalDateTime buildStartTime(LastLoadInfo lastLoadInfo) {
		LocalDateTime startTime = LocalDate.now(ZoneId.of("UTC+1")).atStartOfDay();
		if (lastLoadInfo!=null && lastLoadInfo.getLastLoadDate() !=null) {
			LocalDateTime lastLoadDate = lastLoadInfo.getLastLoadDate();
			startTime = lastLoadDate.getMinute() < 45
					? lastLoadDate.truncatedTo(ChronoUnit.HOURS)
					: lastLoadDate.plusMinutes(15);
		}

		return startTime;
	}


	@Inject
	private LastLoadInfoService lastLoadInfoService;

	@Inject
	private PeriodTimeValueGateway ptGateway;

	@Inject
	private WorkListHeaderService workListHeaderService;

	@Inject
	private BatchHelper batchHelper;
}