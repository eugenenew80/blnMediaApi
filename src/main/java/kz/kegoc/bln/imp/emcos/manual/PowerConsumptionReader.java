package kz.kegoc.bln.imp.emcos.manual;

import kz.kegoc.bln.entity.data.*;
import kz.kegoc.bln.gateway.emcos.MeteringPointCfg;
import kz.kegoc.bln.gateway.emcos.PowerConsumptionGateway;
import kz.kegoc.bln.service.data.BatchService;
import kz.kegoc.bln.service.data.LastLoadInfoService;
import kz.kegoc.bln.service.data.MeteringDataService;
import kz.kegoc.bln.service.data.UserTaskHeaderService;
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
public class PowerConsumptionReader {
	private static final Logger logger = LoggerFactory.getLogger(PowerConsumptionReader.class);

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void read() {
		logger.info("PowerConsumptionReader.read started");

		userTaskHeaderService.findAll().stream()
			.filter(h -> h.getSourceSystemCode().equals("EMCOS") && h.getDirection().equals("IMPORT") && h.getStatus().equals("W"))
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

				logger.info("Request data started");
				List<PowerConsumptionRaw> pcList = pcGateway
					.config(header.getConfig())
					.points(points)
					.request();
				logger.info("Request data completed");

				saveData(batch, pcList);

				logger.info("Update last date");
				lastLoadInfoService.pcUpdateLastDate(batch.getId());

				logger.info("Transfer data");
				lastLoadInfoService.pcLoad(batch.getId());

				endBatch(header, batch, (long) pcList.size());
				logger.info("Import data completed");
			});

		logger.info("PowerConsumptionReader.read completed");
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private Batch startBatch(UserTaskHeader header) {
		logger.info("Create new batch");
		Batch batch = new Batch();
		batch.setUserTaskHeader(header);
		batch.setSourceSystemCode(header.getSourceSystemCode());
		batch.setDirection(header.getDirection());
		batch.setParamType("PC");
		batch.setStatus("P");
		batch.setStartDate(LocalDateTime.now());
		batchService.create(batch);
		return batch;
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private Batch endBatch(UserTaskHeader header, Batch batch, Long recCount) {
		logger.info("Update batch");
		batch.setStatus("C");
		batch.setEndDate(LocalDateTime.now());
		batch.setRecCount(recCount);
		batchService.update(batch);

		logger.info("Update header");
		header = userTaskHeaderService.findById(header.getId());
		header.setBatch(batch);
		userTaskHeaderService.update(header);
		return batch;
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	private void saveData(Batch batch, List<PowerConsumptionRaw> list) {
		logger.info("Save power consumption data in database started");
		list.forEach(t -> t.setBatchId(batch.getId()));
		pcService.saveAll(list);
		logger.info("Save power consumption data in database completed");
	}


	private List<MeteringPointCfg> buildPoints(List<UserTaskLine> lines) {
		List<MeteringPointCfg> points = new ArrayList<>();
		lines.stream()
			.filter(line -> line.getParam().getParamType().equals("PC"))
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
	private PowerConsumptionGateway pcGateway;

	@Inject
	private BatchService batchService;

	@Inject
	private MeteringDataService<PowerConsumptionRaw> pcService;

	@Inject
	private UserTaskHeaderService userTaskHeaderService;
}