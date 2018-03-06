package kz.kegoc.bln.imp.emcos.reader.manual.impl;

import kz.kegoc.bln.entity.common.BatchStatusEnum;
import kz.kegoc.bln.entity.common.DirectionEnum;
import kz.kegoc.bln.entity.common.ParamTypeEnum;
import kz.kegoc.bln.entity.common.SourceSystemEnum;
import kz.kegoc.bln.entity.data.*;
import kz.kegoc.bln.gateway.emcos.MeteringPointCfg;
import kz.kegoc.bln.gateway.emcos.AtTimeValueGateway;
import kz.kegoc.bln.imp.emcos.reader.BatchHelper;
import kz.kegoc.bln.imp.emcos.reader.manual.ManualReader;
import kz.kegoc.bln.service.data.BatchService;
import kz.kegoc.bln.service.data.LastLoadInfoService;
import kz.kegoc.bln.service.data.WorkListHeaderService;
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
public class ManualAtTimeValueReader implements ManualReader<AtTimeValueRaw> {
	private static final Logger logger = LoggerFactory.getLogger(ManualAtTimeValueReader.class);

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void read() {
		logger.debug("ManualAtTimeValueReader.read started");

		workListHeaderService.findAll().stream()
			.filter(h -> h.getActive()
				&& BatchStatus.newInstance(BatchStatusEnum.W).equals(h.getAtStatus())
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

				Batch batch = batchHelper.createBatch(new Batch(header, ParamTypeEnum.AT));

				Long recCount = 0l;
				try {
					List<AtTimeValueRaw> atList = atGateway
						.config(header.getConfig())
						.points(points)
						.request();

					batchHelper.saveAtData(batch, atList);
					recCount = recCount + atList.size();

					lastLoadInfoService.mrUpdateLastDate(batch.getId());
					lastLoadInfoService.mrLoad(batch.getId());
					batchHelper.updateBatch(batch, null, recCount);
				}
				catch (Exception e) {
					logger.error("ManualAtTimeValueReader.read failed: " + e.getMessage());
					batchHelper.updateBatch(batch, e, null);
				}
			});

		logger.debug("ManualAtTimeValueReader.read completed");
	}


	private List<MeteringPointCfg> buildPoints(List<WorkListLine> lines) {
		List<MeteringPointCfg> points = new ArrayList<>();
		lines.stream()
			.filter(line -> line.getParam().getParamType().equals(newInstance(ParamTypeEnum.AT)))
			.forEach(line -> {
				MeteringPointCfg mpc = buildPointCfg(line);
				if (mpc!=null)
					points.add(mpc);
			});

		return points;
	}


	private MeteringPointCfg buildPointCfg(WorkListLine line) {
		ParameterConf parameterConf = line.getParam().getConfs()
			.stream()
			.filter(c -> c.getSourceSystemCode().equals(SourceSystem.newInstance(SourceSystemEnum.EMCOS)))
			.findFirst()
			.orElse(null);

		if (parameterConf!=null) {
			MeteringPointCfg mpc = new MeteringPointCfg();
			mpc.setSourceParamCode(parameterConf.getSourceParamCode());
			mpc.setSourceUnitCode(parameterConf.getSourceUnitCode());
			mpc.setInterval(parameterConf.getInterval());
			mpc.setSourceMeteringPointCode(line.getMeteringPoint().getExternalCode());
			mpc.setParamCode(line.getParam().getCode());
			mpc.setStartTime(line.getStartDate());
			mpc.setEndTime(line.getEndDate());
			if (!(mpc.getStartTime().isEqual(mpc.getEndTime()) || mpc.getStartTime().isAfter(mpc.getEndTime())))
				return mpc;
		}

		return null;
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
