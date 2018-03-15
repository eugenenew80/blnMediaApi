package kz.kegoc.bln.exp.emcos.sender;

import kz.kegoc.bln.ejb.cdi.annotation.SOAP;
import kz.kegoc.bln.entity.common.BatchStatusEnum;
import kz.kegoc.bln.entity.common.DirectionEnum;
import kz.kegoc.bln.entity.common.ParamTypeEnum;
import kz.kegoc.bln.entity.common.SourceSystemEnum;
import kz.kegoc.bln.entity.data.*;
import kz.kegoc.bln.exp.ftp.sender.Sender;
import kz.kegoc.bln.gateway.emcos.MeteringPointCfg;
import kz.kegoc.bln.gateway.emcos.PeriodTimeValueExpGateway;
import kz.kegoc.bln.imp.emcos.reader.BatchHelper;
import kz.kegoc.bln.service.data.WorkListHeaderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


@Stateless
@SOAP
public class SoapPeriodTimeValueSender implements Sender<PeriodTimeValue> {
	private static final Logger logger = LoggerFactory.getLogger(SoapPeriodTimeValueSender.class);

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void send() {
		AtomicBoolean flag = new AtomicBoolean(false);

		workListHeaderService.findAll().stream()
			.filter(h -> h.getActive()
				&& SourceSystem.newInstance(SourceSystemEnum.EMCOS).equals(h.getSourceSystemCode())
				&& Direction.newInstance(DirectionEnum.EXPORT).equals(h.getDirection())
				&& (BatchStatus.newInstance(BatchStatusEnum.W).equals(h.getPtStatus()))
				&& StringUtils.equals(h.getWorkListType(), "SYS")
				&& h.getConfig()!=null
			)
			.forEach(header -> {
				if (!flag.get())
					logger.info("SoapPeriodTimeValueSender.read started");

				flag.set(true);

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
				try {
					ptGateway
						.config(header.getConfig())
						.points(points)
						.send();

					batchHelper.updateBatch(batch, null, (long)points.size());
				}
				catch (Exception e) {
					logger.error("SoapPeriodTimeValueSender.send failed: " + e.getMessage());
					batchHelper.updateBatch(batch, e, null);
				}
			});

		if (flag.get())
			logger.info("SoapPeriodTimeValueSender.send completed");
	}


	private List<MeteringPointCfg> buildPoints(List<WorkListLine> lines) {
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH");

		List<MeteringPointCfg> points = new ArrayList<>();
		/*
		lines.stream()
			.filter(line -> line.getParam().getParamType().equals(newInstance(ParamTypeEnum.PT)))
			.forEach(line -> {
				MeteringPointCfg mpc = new MeteringPointCfg();
				points.add(mpc);
			});
		*/

		MeteringPointCfg mpc = new MeteringPointCfg();
		mpc.setSourceParamCode("709");
		mpc.setParamCode("A+");
		mpc.setInterval(3600);
		mpc.setVal(123d);
		mpc.setSourceMeteringPointCode("113440990999999999");

		LocalDateTime startTime = LocalDateTime.parse("12.03.2018 00", timeFormatter);
		LocalDateTime endTime   = LocalDateTime.parse("12.03.2018 01", timeFormatter);

		mpc.setStartTime(startTime);
		mpc.setEndTime(endTime);
		points.add(mpc);

		return points;
	}


	@Inject
	private PeriodTimeValueExpGateway ptGateway;

	@Inject
	private WorkListHeaderService workListHeaderService;

	@Inject
	private BatchHelper batchHelper;
}