package kz.kegoc.bln.imp.emcos.reader.impl;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import javax.ejb.*;
import javax.inject.Inject;
import kz.kegoc.bln.entity.data.*;
import kz.kegoc.bln.gateway.emcos.*;
import kz.kegoc.bln.entity.data.PowerConsumptionRaw;
import kz.kegoc.bln.imp.emcos.reader.Reader;
import kz.kegoc.bln.service.data.LastLoadInfoService;
import kz.kegoc.bln.service.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

@Stateless
public class PowerConsumptionReader implements Reader<PowerConsumptionRaw> {
	private static final Logger logger = LoggerFactory.getLogger(PowerConsumptionReader.class);

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void read() {
		logger.info("PowerConsumptionReader.read started");

		workListHeaderService.findAll().stream()
			.filter(h -> h.getActive() && h.getSourceSystemCode().equals("EMCOS") && h.getDirection().equals("IMPORT") && h.getConfig()!=null)
			.forEach(header -> {
				logger.info("Import data started");
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
				for (int i = 0; i < groupsPoints.size(); i++) {
					List<MeteringPointCfg> groupPoints = groupsPoints.get(i);
					logger.info("group of points num: " + (i+1));
					logger.info("group of points size: " + groupPoints.size());

					logger.info("Request data started");
					List<PowerConsumptionRaw> pcList = powerConsumptionGateway
						.config(header.getConfig())
						.points(groupPoints)
						.request();
					logger.info("Request data completed");

					saveData(batch, pcList);
					recCount = recCount + pcList.size();
				}

				logger.info("Update last date");
				lastLoadInfoService.pcUpdateLastDate(batch.getId());

				logger.info("Transfer data");
				lastLoadInfoService.pcLoad(batch.getId());

				endBatch(header, batch, recCount);
				logger.info("Import data completed");
			});

		logger.info("PowerConsumptionReader.read completed");
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private Batch startBatch(WorkListHeader header) {
		logger.info("Create new batch");
		Batch batch = new Batch();
		batch.setWorkListHeader(header);
		batch.setSourceSystemCode("EMCOS");
		batch.setDirection("IMPORT");
		batch.setDataType("PC");
		batch.setStatus("P");
		batch.setStartDate(LocalDateTime.now());
		batchService.create(batch);
		return batch;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private Batch endBatch(WorkListHeader header, Batch batch, Long recCount) {
		logger.info("Update batch");
		batch.setStatus("C");
		batch.setEndDate(LocalDateTime.now());
		batch.setRecCount(recCount);
		batchService.update(batch);

		logger.info("Update header");
		header = workListHeaderService.findById(header.getId());
		header.setPcLastBatch(batch);
		workListHeaderService.update(header);
		return batch;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void saveData(Batch batch, List<PowerConsumptionRaw> list) {
		logger.info("Save power consumption data in database started");
		list.forEach(t -> t.setBatchId(batch.getId()));
		pcService.saveAll(list);
		logger.info("Save power consumption data in database completed");
	}

	private List<MeteringPointCfg> buidPoints(List<WorkListLine> lines) {
		List<LastLoadInfo> lastLoadInfoList = lastLoadInfoService.findAll();

		List<MeteringPointCfg> points = new ArrayList<>();
		for (WorkListLine line : lines) {
			MeteringPointCfg mpc = new MeteringPointCfg();
			mpc.setPointCode(line.getMeteringPoint().getExternalCode());
			mpc.setParamCode(line.getParam().getCode());
			mpc.setEmcosParamCode(line.getParam().getSourceParamCode());
			mpc.setUnitCode(line.getParam().getSourceUnitCode());

			LastLoadInfo lastLoadInfo = lastLoadInfoList.stream()
				.filter(t -> t.getSourceMeteringPointCode().equals(mpc.getPointCode()) && t.getSourceParamCode().equals(mpc.getEmcosParamCode()))
				.findFirst()
				.orElse(null);

			mpc.setStartTime(buildStartTime(lastLoadInfo));
			mpc.setEndTime(buildRequestedTime());

			if (!(mpc.getStartTime().isEqual(mpc.getEndTime()) || mpc.getStartTime().isAfter(mpc.getEndTime())))
				points.add(mpc);

		}
		return points;
	}

	private LocalDateTime buildRequestedTime() {
		return LocalDateTime.now(ZoneId.of("UTC+1"))
					.minusMinutes(15)
					.truncatedTo(ChronoUnit.HOURS);
	}

	private LocalDateTime buildStartTime(LastLoadInfo lastLoadInfo) {
		LocalDateTime startTime = LocalDate.now(ZoneId.of("UTC+1")).atStartOfDay().minusDays(2);
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
	private PowerConsumptionGateway powerConsumptionGateway;

	@Inject
	private WorkListHeaderService workListHeaderService;

	@Inject
	private ParameterService parameterService;

	@Inject
	private BatchService batchService;

	@Inject
	private MeteringDataService<PowerConsumptionRaw> pcService;
}