package kz.kegoc.bln.imp.emcos.reader.manual.impl;

import kz.kegoc.bln.entity.common.BatchStatusEnum;
import kz.kegoc.bln.entity.common.DirectionEnum;
import kz.kegoc.bln.entity.common.ParamTypeEnum;
import kz.kegoc.bln.entity.common.SourceSystemEnum;
import kz.kegoc.bln.entity.data.*;
import kz.kegoc.bln.gateway.emcos.MeteringPointCfg;
import kz.kegoc.bln.gateway.emcos.PeriodTimeValueGateway;
import kz.kegoc.bln.imp.emcos.reader.BatchHelper;
import kz.kegoc.bln.imp.emcos.reader.manual.ManualReader;
import kz.kegoc.bln.service.data.LastLoadInfoService;
import kz.kegoc.bln.service.data.MeteringValueService;
import kz.kegoc.bln.service.data.UserTaskHeaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import static kz.kegoc.bln.entity.data.ParamType.newInstance;

@Stateless
public class ManualPeriodTimeReader implements ManualReader<PeriodTimeValueRaw> {
	private static final Logger logger = LoggerFactory.getLogger(ManualPeriodTimeReader.class);

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void read() {
		logger.debug("ManualPeriodTimeReader.read started");

		userTaskHeaderService.findAll().stream()
			.filter(h -> h.getActive()
				&& BatchStatus.newInstance(BatchStatusEnum.W).equals(h.getPtStatus())
				&& SourceSystem.newInstance(SourceSystemEnum.EMCOS).equals(h.getSourceSystemCode())
				&& Direction.newInstance(DirectionEnum.IMPORT).equals(h.getDirection())
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
		return batchHelper.startBatch(header, ParamTypeEnum.PT);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private Batch endBatch(UserTaskHeader header, Batch batch, Long recCount) {
		return batchHelper.endBatch(header, batch, recCount);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private Batch errorBatch(UserTaskHeader header, Batch batch, Exception e) {
		return batchHelper.errorBatch(header, batch, e);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void saveData(Batch batch, List<PeriodTimeValueRaw> list) {
		list.forEach(t -> t.setBatch(batch));
		pcService.saveAll(list);
	}

	private List<MeteringPointCfg> buildPoints(List<UserTaskLine> lines) {
		List<MeteringPointCfg> points = new ArrayList<>();
		lines.stream()
			.filter(line -> line.getParam().getParamType().equals(newInstance(ParamTypeEnum.PT)))
			.forEach(line -> {
				MeteringPointCfg mpc = batchHelper.buildPointCfg(line);
				if (mpc!=null)
					points.add(mpc);
			});

		return points;
	}


	@Inject
	private LastLoadInfoService lastLoadInfoService;

	@Inject
	private PeriodTimeValueGateway pcGateway;

	@Inject
	private MeteringValueService<PeriodTimeValueRaw> pcService;

	@Inject
	private UserTaskHeaderService userTaskHeaderService;

	@Inject
	private BatchHelper batchHelper;
}
