package kz.kegoc.bln.imp.emcos.reader.auto.impl;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import javax.ejb.*;
import javax.inject.Inject;
import kz.kegoc.bln.entity.data.*;
import kz.kegoc.bln.gateway.emcos.*;
import kz.kegoc.bln.entity.data.PeriodTimeValueRaw;
import kz.kegoc.bln.imp.emcos.reader.auto.AutoReader;
import kz.kegoc.bln.service.data.LastLoadInfoService;
import kz.kegoc.bln.service.data.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

@Stateless
public class AutoPeriodTimeValueReader implements AutoReader<PeriodTimeValueRaw> {
	private static final Logger logger = LoggerFactory.getLogger(AutoPeriodTimeValueReader.class);

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void read() {
		logger.info("AutoPeriodTimeValueReader.read started");

		workListHeaderService.findAll().stream()
			.filter(h -> h.getActive()
				&& StringUtils.equals(h.getSourceSystemCode(), "EMCOS")
				&& StringUtils.equals(h.getDirection(),"IMPORT")
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

				List<MeteringPointCfg> points = buidPoints(header.getLines());
				if (points.size()==0) {
					logger.info("Import data is not required, import data stopped");
					return;
				}

				Batch batch = startBatch(header);

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

						List<PeriodTimeValueRaw> pcList = powerConsumptionGateway
								.config(header.getConfig())
								.points(groupPoints)
								.request();

						saveData(batch, pcList);
						recCount = recCount + pcList.size();
					}

					lastLoadInfoService.pcUpdateLastDate(batch.getId());
					lastLoadInfoService.pcLoad(batch.getId());
					endBatch(header, batch, recCount);
				}
				catch (Exception e) {
					logger.error("AutoPeriodTimeValueReader.read failed: " + e.getMessage());
					errorBatch(header, batch, e);
				}
			});

		logger.info("AutoPeriodTimeValueReader.read completed");
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private Batch startBatch(WorkListHeader header) {
		Batch batch = new Batch();
		batch.setWorkListHeader(header);
		batch.setSourceSystemCode(header.getSourceSystemCode());
		batch.setDirection(header.getDirection());
		batch.setParamType("PT");
		batch.setStatus("P");
		batch.setStartDate(LocalDateTime.now());
		batch = batchService.create(batch);

		header = workListHeaderService.findById(header.getId());
		header.setPtBatch(batch);
		header.setPtStatus("P");
		workListHeaderService.update(header);

		return batch;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private Batch endBatch(WorkListHeader header, Batch batch, Long recCount) {
		batch.setStatus("C");
		batch.setEndDate(LocalDateTime.now());
		batch.setRecCount(recCount);
		batchService.update(batch);

		header = workListHeaderService.findById(header.getId());
		header.setPtStatus("C");
		workListHeaderService.update(header);
		return batch;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private Batch errorBatch(WorkListHeader header, Batch batch, Exception e) {
		batch.setStatus("E");
		batch.setEndDate(LocalDateTime.now());
		batch.setErrMsg(e.getMessage());
		batchService.update(batch);

		header = workListHeaderService.findById(header.getId());
		header.setPtStatus("E");
		workListHeaderService.update(header);
		return batch;
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void saveData(Batch batch, List<PeriodTimeValueRaw> list) {
		list.forEach(t -> t.setBatchId(batch.getId()));
		pcService.saveAll(list);
	}

	private List<MeteringPointCfg> buidPoints(List<WorkListLine> lines) {
		List<LastLoadInfo> lastLoadInfoList = lastLoadInfoService.findAll();

		List<MeteringPointCfg> points = new ArrayList<>();
		lines.stream()
			.filter(line -> line.getParam().getParamType().equals("PT"))
			.forEach(line -> {
				ParameterConf parameterConf = line.getParam().getConfs()
					.stream()
					.filter(c -> c.getSourceSystemCode().equals("EMCOS"))
					.findFirst()
					.orElse(null);

				if (parameterConf!=null) {
					MeteringPointCfg mpc = new MeteringPointCfg();
					mpc.setSourceParamCode(parameterConf.getSourceParamCode());
					mpc.setSourceUnitCode(parameterConf.getSourceUnitCode());
					mpc.setInterval(parameterConf.getInterval());
					mpc.setSourceMeteringPointCode(line.getMeteringPoint().getExternalCode());
					mpc.setParamCode(line.getParam().getCode());

					LastLoadInfo lastLoadInfo = lastLoadInfoList.stream()
						.filter(t -> t.getSourceMeteringPointCode().equals(mpc.getSourceMeteringPointCode()) && t.getSourceParamCode().equals(mpc.getSourceParamCode()))
						.findFirst()
						.orElse(null);

					mpc.setStartTime(buildStartTime(lastLoadInfo));
					mpc.setEndTime(buildRequestedTime());
					if (!(mpc.getStartTime().isEqual(mpc.getEndTime()) || mpc.getStartTime().isAfter(mpc.getEndTime())))
						points.add(mpc);
				}
			});
		return points;
	}

	private LocalDateTime buildRequestedTime() {
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
	private PeriodTimeValueGateway powerConsumptionGateway;

	@Inject
	private WorkListHeaderService workListHeaderService;

	@Inject
	private BatchService batchService;

	@Inject
	private MeteringValueService<PeriodTimeValueRaw> pcService;
}