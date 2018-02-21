package kz.kegoc.bln.imp.emcos.reader.manual.impl;

import kz.kegoc.bln.entity.data.*;
import kz.kegoc.bln.gateway.emcos.MeteringPointCfg;
import kz.kegoc.bln.gateway.emcos.PeriodTimeValueGateway;
import kz.kegoc.bln.imp.emcos.reader.manual.ManualReader;
import kz.kegoc.bln.service.data.BatchService;
import kz.kegoc.bln.service.data.LastLoadInfoService;
import kz.kegoc.bln.service.data.MeteringValueService;
import kz.kegoc.bln.service.data.UserTaskHeaderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ManualPeriodTimeReader implements ManualReader<PeriodTimeValueRaw> {
	private static final Logger logger = LoggerFactory.getLogger(ManualPeriodTimeReader.class);

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void read() {
		logger.debug("ManualPeriodTimeReader.read started");

		userTaskHeaderService.findAll().stream()
			.filter(h -> h.getActive()
				&& StringUtils.equals(h.getPtStatus(), "W")
				&& StringUtils.equals(h.getSourceSystemCode(), "EMCOS")
				&& StringUtils.equals(h.getDirection(),"IMPORT")
				&& h.getConfig()!=null
			)
			.forEach(header -> {
				logger.info("Import data started");
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

				Long recCount = 0l;
				try {
					List<PeriodTimeValueRaw> pcList = pcGateway
						.config(header.getConfig())
						.points(points)
						.request();

					saveData(batch, pcList);
					recCount = recCount + pcList.size();

					lastLoadInfoService.pcUpdateLastDate(batch.getId());
					lastLoadInfoService.pcLoad(batch.getId());
					endBatch(header, batch, recCount);
				}
				catch (Exception e) {
					logger.error("ManualPeriodTimeReader.read failed: " + e.getMessage());
					errorBatch(header, batch, e);
				}
			});

		logger.debug("ManualPeriodTimeReader.read completed");
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private Batch startBatch(UserTaskHeader header) {
		Batch batch = new Batch();
		batch.setUserTaskHeader(header);
		batch.setSourceSystemCode(header.getSourceSystemCode());
		batch.setDirection(header.getDirection());
		batch.setParamType("PT");
		batch.setStatus("P");
		batch.setStartDate(LocalDateTime.now());
		batch = batchService.create(batch);

		header = userTaskHeaderService.findById(header.getId());
		header.setPtBatch(batch);
		header.setPtStatus("P");
		userTaskHeaderService.update(header);

		return batch;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private Batch endBatch(UserTaskHeader header, Batch batch, Long recCount) {
		batch.setStatus("C");
		batch.setEndDate(LocalDateTime.now());
		batch.setRecCount(recCount);
		batchService.update(batch);

		header = userTaskHeaderService.findById(header.getId());
		header.setPtBatch(batch);
		header.setPtStatus("C");
		userTaskHeaderService.update(header);
		return batch;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private Batch errorBatch(UserTaskHeader header, Batch batch, Exception e) {
		batch.setStatus("E");
		batch.setEndDate(LocalDateTime.now());
		batch.setErrMsg(e.getMessage());
		batchService.update(batch);

		header = userTaskHeaderService.findById(header.getId());
		header.setPtBatch(batch);
		header.setPtStatus("E");
		userTaskHeaderService.update(header);
		return batch;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void saveData(Batch batch, List<PeriodTimeValueRaw> list) {
		list.forEach(t -> t.setBatchId(batch.getId()));
		pcService.saveAll(list);
	}

	private List<MeteringPointCfg> buildPoints(List<UserTaskLine> lines) {
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
					mpc.setStartTime(line.getStartMeteringDate());
					mpc.setEndTime(line.getEndMeteringDate());
					if (!(mpc.getStartTime().isEqual(mpc.getEndTime()) || mpc.getStartTime().isAfter(mpc.getEndTime())))
						points.add(mpc);
				}
			});
		return points;
	}


	@Inject
	private LastLoadInfoService lastLoadInfoService;

	@Inject
	private PeriodTimeValueGateway pcGateway;

	@Inject
	private BatchService batchService;

	@Inject
	private MeteringValueService<PeriodTimeValueRaw> pcService;

	@Inject
	private UserTaskHeaderService userTaskHeaderService;
}
