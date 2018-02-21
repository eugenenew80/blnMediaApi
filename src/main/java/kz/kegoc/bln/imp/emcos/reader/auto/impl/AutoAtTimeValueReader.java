package kz.kegoc.bln.imp.emcos.reader.auto.impl;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import javax.ejb.*;
import javax.inject.Inject;
import kz.kegoc.bln.entity.data.*;
import kz.kegoc.bln.gateway.emcos.AtTimeValueGateway;
import kz.kegoc.bln.gateway.emcos.MeteringPointCfg;
import kz.kegoc.bln.imp.emcos.reader.auto.AutoReader;
import kz.kegoc.bln.service.data.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

@Stateless
public class AutoAtTimeValueReader implements AutoReader<AtTimeValueRaw> {
	private static final Logger logger = LoggerFactory.getLogger(AutoAtTimeValueReader.class);

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void read() {
		logger.info("AutoAtTimeValueReader.read started");

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

				List<MeteringPointCfg> points = buildPoints(header.getLines());
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

						List<AtTimeValueRaw> mrList = meteringReadingGateway
							.config(header.getConfig())
							.points(groupPoints)
							.request();

						saveData(batch, mrList);
						recCount = recCount + mrList.size();
					}

					lastLoadInfoService.mrUpdateLastDate(batch.getId());
					lastLoadInfoService.mrLoad(batch.getId());
					endBatch(header, batch, recCount);
				}
				catch (Exception e) {
					logger.error("AutoAtTimeValueReader.read failed: " + e.getMessage());
					errorBatch(header, batch, e);
				}
			});

		logger.info("AutoAtTimeValueReader.read completed");
    }


	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private Batch startBatch(WorkListHeader header) {
		Batch batch = new Batch();
		batch.setWorkListHeader(header);
		batch.setSourceSystemCode(header.getSourceSystemCode());
		batch.setDirection(header.getDirection());
		batch.setParamType("AT");
		batch.setStatus("P");
		batch.setStartDate(LocalDateTime.now());
		batch = batchService.create(batch);

		header = workListHeaderService.findById(header.getId());
		header.setAtBatch(batch);
		header.setAtStatus("P");
		workListHeaderService.update(header);
		return batch;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private Batch endBatch(WorkListHeader header, Batch batch, Long retCount) {
		batch.setStatus("C");
		batch.setEndDate(LocalDateTime.now());
		batch.setRecCount(retCount);
		batchService.update(batch);

		header = workListHeaderService.findById(header.getId());
		header.setAtBatch(batch);
		header.setAtStatus("C");
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
		header.setAtBatch(batch);
		header.setAtStatus("E");
		workListHeaderService.update(header);
		return batch;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void saveData(Batch batch, List<AtTimeValueRaw> list) {
		list.forEach(t -> t.setBatchId(batch.getId()));
		mrService.saveAll(list);
	}

    private LocalDateTime buildRequestedDateTime() {
		return LocalDate.now(ZoneId.of("UTC+1"))
					.plusDays(1)
					.atStartOfDay();
	}

	private LocalDateTime buildStartTime(LastLoadInfo lastLoadInfo) {
		if (lastLoadInfo!=null && lastLoadInfo.getLastLoadDate()!=null)
			return lastLoadInfo.getLastLoadDate().plusDays(1).truncatedTo(ChronoUnit.DAYS);
		else
			return LocalDate.now(ZoneId.of("UTC+1")).atStartOfDay();
	}

	private List<MeteringPointCfg> buildPoints(List<WorkListLine> lines) {
		List<LastLoadInfo> lastLoadInfoList = lastLoadInfoService.findAll();

		List<MeteringPointCfg> points= new ArrayList<>();
		lines.stream()
			.filter(l -> l.getParam().getParamType().equals("AT"))
			.forEach(line -> {
				ParameterConf parameterConf = line.getParam().getConfs()
					.stream()
					.filter(c -> c.getSourceSystemCode().equals("EMCOS"))
					.findFirst()
					.orElse(null);

				if (parameterConf!=null) {
					MeteringPointCfg mpc = new MeteringPointCfg();
					mpc.setSourceMeteringPointCode(line.getMeteringPoint().getExternalCode());
					mpc.setParamCode(line.getParam().getCode());
					mpc.setSourceParamCode(parameterConf.getSourceParamCode());
					mpc.setInterval(parameterConf.getInterval());
					mpc.setSourceUnitCode(parameterConf.getSourceUnitCode());

					LastLoadInfo lastLoadInfo = lastLoadInfoList.stream()
						.filter(t -> t.getSourceMeteringPointCode().equals(mpc.getSourceMeteringPointCode()) && t.getSourceParamCode().equals(mpc.getSourceParamCode()))
						.findFirst()
						.orElse(null);

					mpc.setStartTime(buildStartTime(lastLoadInfo));
					mpc.setEndTime(buildRequestedDateTime());
					if (!(mpc.getStartTime().isEqual(mpc.getEndTime()) || mpc.getStartTime().isAfter(mpc.getEndTime())))
						points.add(mpc);
				}
			});
		return points;
	}


	@Inject
	private LastLoadInfoService lastLoadInfoService;

	@Inject
	private AtTimeValueGateway meteringReadingGateway;

	@Inject
	private WorkListHeaderService workListHeaderService;

	@Inject
	private BatchService batchService;

	@Inject
	private MeteringValueService<AtTimeValueRaw> mrService;
}
