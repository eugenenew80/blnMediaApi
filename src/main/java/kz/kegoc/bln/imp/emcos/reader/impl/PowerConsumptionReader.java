package kz.kegoc.bln.imp.emcos.reader.impl;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
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

@Stateless
public class PowerConsumptionReader implements Reader<PowerConsumptionRaw> {
	private static final Logger logger = LoggerFactory.getLogger(PowerConsumptionReader.class);

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void read() {
		List<Parameter> params = parameterService.findAll().stream()
			.filter( p -> p.getParamType().equals("PC"))
			.collect(Collectors.toList());

		logger.info("PowerConsumptionReader.read started");
		logger.info("Parameters: " + params.stream().map(Parameter::getCode).collect(Collectors.joining(", ")));

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

				List<MeteringPointCfg> points = buidPoints(params, header.getLines());
				if (points.size()==0) {
					logger.info("Import data is not required, import data stopped");
					return;
				}

				Batch batch = start(header);

				logger.info("Request data started");
				List<PowerConsumptionRaw> pcList = powerConsumptionGateway
					.config(header.getConfig())
					.points(points)
					.request();
				logger.info("Request data completed");

				save(header, batch, pcList);

				logger.info("Update last date");
				lastLoadInfoService.pcUpdateLastDate(batch.getId());

				logger.info("Transfer date");
				lastLoadInfoService.pcLoad(batch.getId());

				end(header, batch, pcList);

				logger.info("Import data completed");
			});

		logger.info("PowerConsumptionReader.read completed");
    }


	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private Batch start(WorkListHeader header) {
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
	private Batch end(WorkListHeader header, Batch batch, List<PowerConsumptionRaw> list) {
		logger.info("Update batch");
		batch.setStatus("C");
		batch.setEndDate(LocalDateTime.now());
		batch.setRecCount((long)list.size());
		batchService.update(batch);

		logger.info("Update header");
		header = workListHeaderService.findById(header.getId());
		header.setPcLastBatch(batch);
		workListHeaderService.update(header);
		return batch;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void save(WorkListHeader header, Batch batch, List<PowerConsumptionRaw> list) {
		logger.info("Save data in db started");
		Long batchId = batch.getId();
		list.forEach(t -> t.setBatchId(batchId));
		pcService.saveAll(list);
		logger.info("Save data in db completed");
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

	private List<MeteringPointCfg> buidPoints(List<Parameter> params, List<WorkListLine> lines) {
		List<LastLoadInfo> lastLoadInfoList = lastLoadInfoService.findAll();

		List<MeteringPointCfg> points = new ArrayList<>();
		for (Parameter param : params) {
			for (WorkListLine line : lines) {
				MeteringPointCfg mpc = new MeteringPointCfg();
				mpc.setPointCode(line.getMeteringPoint().getExternalCode());
				mpc.setParamCode(param.getCode());
				mpc.setEmcosParamCode(param.getSourceParamCode());
				mpc.setUnitCode(param.getSourceUnitCode());

				LastLoadInfo lastLoadInfo = lastLoadInfoList.stream()
					.filter(t -> t.getSourceMeteringPointCode().equals(mpc.getPointCode()) && t.getSourceParamCode().equals(mpc.getEmcosParamCode()))
					.findFirst()
					.orElse(null);

				mpc.setStartTime(buildStartTime(lastLoadInfo));
				mpc.setEndTime(buildRequestedTime());

				if (!(mpc.getStartTime().isEqual(mpc.getEndTime()) || mpc.getStartTime().isAfter(mpc.getEndTime())))
					points.add(mpc);
			}
		}
		return points;
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
