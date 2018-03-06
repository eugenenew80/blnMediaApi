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
				ParameterConf parameterConf = line.getParam().getConfs()
					.stream()
					.filter(c -> c.getSourceSystemCode().equals(SourceSystem.newInstance(SourceSystemEnum.EMCOS)))
					.findFirst()
					.orElse(null);

				LastLoadInfo lastLoadInfo = lastLoadInfoList.stream()
					.filter(t -> t.getSourceMeteringPointCode().equals(line.getMeteringPoint().getExternalCode()) && t.getSourceParamCode().equals(parameterConf.getSourceParamCode()))
					.findFirst()
					.orElse(null);

				MeteringPointCfg mpc = buildPointCfg(line, parameterConf, lastLoadInfo);
				if (mpc!=null)
					points.add(mpc);
			});

		return points;
	}

	private MeteringPointCfg buildPointCfg(WorkListLine line, ParameterConf parameterConf, LastLoadInfo lastLoadInfo) {
		if (parameterConf!=null) {
			MeteringPointCfg mpc = new MeteringPointCfg();
			mpc.setSourceMeteringPointCode(line.getMeteringPoint().getExternalCode());
			mpc.setSourceParamCode(parameterConf.getSourceParamCode());
			mpc.setSourceUnitCode(parameterConf.getSourceUnitCode());
			mpc.setInterval(parameterConf.getInterval());
			mpc.setParamCode(line.getParam().getCode());
			mpc.setStartTime(buildStartTime(lastLoadInfo));
			mpc.setEndTime(buildRequestedDateTime());
			if (!(mpc.getStartTime().isEqual(mpc.getEndTime()) || mpc.getStartTime().isAfter(mpc.getEndTime())))
				return mpc;
		}

		return null;
	}

	private LocalDateTime buildRequestedDateTime() {
		return LocalDateTime.now(ZoneId.of("UTC+1"))
				.minusMinutes(15)
				.truncatedTo(ChronoUnit.HOURS);
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