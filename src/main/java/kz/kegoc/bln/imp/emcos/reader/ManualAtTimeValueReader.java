package kz.kegoc.bln.imp.emcos.reader;

import kz.kegoc.bln.ejb.annotation.Emcos;
import kz.kegoc.bln.ejb.annotation.Manual;
import kz.kegoc.bln.common.enums.BatchStatusEnum;
import kz.kegoc.bln.common.enums.DirectionEnum;
import kz.kegoc.bln.common.enums.ParamTypeEnum;
import kz.kegoc.bln.common.enums.SourceSystemEnum;
import kz.kegoc.bln.entity.media.*;
import kz.kegoc.bln.gateway.emcos.MeteringPointCfg;
import kz.kegoc.bln.gateway.emcos.AtTimeValueGateway;
import kz.kegoc.bln.imp.BatchHelper;
import kz.kegoc.bln.imp.Reader;
import kz.kegoc.bln.imp.raw.AtTimeValueRaw;
import kz.kegoc.bln.service.LastLoadInfoService;
import kz.kegoc.bln.service.WorkListHeaderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import static kz.kegoc.bln.entity.media.ParamType.newInstance;

@Stateless
@Emcos @Manual
public class ManualAtTimeValueReader implements Reader<AtTimeValueRaw> {
	private static final Logger logger = LoggerFactory.getLogger(ManualAtTimeValueReader.class);

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void read() {
		logger.debug("read started");

		workListHeaderService.findAll().stream()
			.filter(h -> h.getActive()
				&& BatchStatus.newInstance(BatchStatusEnum.W).equals(h.getAtStatus())
				&& SourceSystem.newInstance(SourceSystemEnum.EMCOS).equals(h.getSourceSystemCode())
				&& Direction.newInstance(DirectionEnum.IMPORT).equals(h.getDirection())
				&& StringUtils.equals(h.getWorkListType(), "USER")
				&& h.getConfig()!=null
			)
			.forEach(header -> {
				logger.info("Import media started");
				logger.info("headerId: " + header.getId());
				logger.info("url: " + header.getConfig().getUrl());
				logger.info("user: " + header.getConfig().getUserName());

				if (header.getLines().size()==0) {
					logger.info("List of points is empty, import media stopped");
					return;
				}

				List<MeteringPointCfg> points = buildPoints(header.getLines());
				if (points.size()==0) {
					logger.info("Import media is not required, import media stopped");
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

					batchHelper.updateBatch(batch, null, recCount);
					lastLoadInfoService.atUpdateLastDate(batch.getId());
					lastLoadInfoService.atLoad(batch.getId());
				}
				catch (Exception e) {
					logger.error("read failed: " + e.getMessage());
					batchHelper.updateBatch(batch, e, null);
				}
			});

		logger.debug("read completed");
	}


	private List<MeteringPointCfg> buildPoints(List<WorkListLine> lines) {
		List<MeteringPointCfg> points = new ArrayList<>();
		lines.stream()
			.filter(line -> line.getParam().getIsAt())
			.forEach(line -> {
				MeteringPointCfg mpc = batchHelper.buildPointCfg(
					line,
					line.getStartDate(),
					line.getEndDate(),
					ParamTypeEnum.AT,
					null
				);

				if (!(mpc.getStartTime().isEqual(mpc.getEndTime()) || mpc.getStartTime().isAfter(mpc.getEndTime())))
					points.add(mpc);
			});

		return points;
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
