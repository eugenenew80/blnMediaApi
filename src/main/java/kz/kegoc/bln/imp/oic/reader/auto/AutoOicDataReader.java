package kz.kegoc.bln.imp.oic.reader.auto;

import kz.kegoc.bln.ejb.cdi.annotation.Auto;
import kz.kegoc.bln.ejb.cdi.annotation.Oic;
import kz.kegoc.bln.entity.common.*;
import kz.kegoc.bln.entity.data.*;
import kz.kegoc.bln.gateway.emcos.MeteringPointCfg;
import kz.kegoc.bln.gateway.oic.OicDataImpGateway;
import kz.kegoc.bln.imp.BatchHelper;
import kz.kegoc.bln.imp.Reader;
import kz.kegoc.bln.imp.raw.PeriodTimeValueRaw;
import kz.kegoc.bln.imp.raw.TelemetryRaw;
import kz.kegoc.bln.service.data.LastLoadInfoService;
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

import static java.util.stream.Collectors.toList;
import static kz.kegoc.bln.entity.data.ParamType.newInstance;

@Stateless
@Oic @Auto
public class AutoOicDataReader implements Reader<TelemetryRaw> {
	private static final Logger logger = LoggerFactory.getLogger(AutoOicDataReader.class);
	private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void read() {
		logger.info("AutoOicDataReader.read started");

		workListHeaderService.findAll().stream()
			.filter(h -> h.getActive()
				&& SourceSystem.newInstance(SourceSystemEnum.OIC).equals(h.getSourceSystemCode())
				&& Direction.newInstance(DirectionEnum.IMPORT).equals(h.getDirection())
				&& StringUtils.equals(h.getWorkListType(), "SYS")
				&& h.getConfig()!=null
			)
			.forEach(header -> {
				logger.info("headerId: " + header.getId());
				logger.info("url: " + header.getConfig().getUrl());
				logger.info("user: " + header.getConfig().getUserName());

				buildPoints(header.getLines());

				Batch batch = batchHelper.createBatch(new Batch(header, ParamTypeEnum.PT));
				try {
					List<PeriodTimeValueRaw> ptList = oicDataImpGateway.request().stream()
						.map(t -> {
							PeriodTimeValueRaw pt = new PeriodTimeValueRaw();
							pt.setInterval(180);
							pt.setSourceParamCode(t.getParamCode());
							pt.setSourceMeteringPointCode(t.getLogPoint().toString());
							pt.setSourceUnitCode(t.getUnitCode());
							pt.setMeteringDate(LocalDateTime.parse(t.getDateTime(), timeFormatter));
							pt.setVal(t.getVal());
							pt.setSourceSystemCode(SourceSystemEnum.OIC);
							pt.setStatus(ProcessingStatusEnum.TMP);
							pt.setInputMethod(InputMethod.newInstance(InputMethodEnum.AUTO));
							pt.setReceivingMethod(ReceivingMethod.newInstance(ReceivingMethodEnum.SERVICE));
							pt.setBatch(batch);
							return pt;
						})
						.collect(toList());
					Long retCount = (long)ptList.size();

					batchHelper.savePtData(batch, ptList);
					lastLoadInfoService.pcUpdateLastDate(batch.getId());
					lastLoadInfoService.pcLoad(batch.getId());
					batchHelper.updateBatch(batch, null, retCount);
				}
				catch (Exception e) {
					logger.error("AutoOicDataReader.read failed: " + e.getMessage());
					batchHelper.updateBatch(batch, e, null);
				}
			});

		logger.info("AutoOicDataReader.read completed");
    }

	private void buildPoints(List<WorkListLine> lines) {
		lastLoadInfoService.findAll().stream()
			.filter(l -> l.getSourceSystemCode().equals("OIC"))
			.forEach(l -> {
				logger.info("sourceMeteringPointCode: " + l.getSourceMeteringPointCode());
			});
	}


	@Inject
	private OicDataImpGateway oicDataImpGateway;

	@Inject
	private WorkListHeaderService workListHeaderService;

	@Inject
	private BatchHelper batchHelper;

	@Inject
	private LastLoadInfoService lastLoadInfoService;
}
