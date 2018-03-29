package kz.kegoc.bln.imp.oic.reader.auto;

import kz.kegoc.bln.ejb.cdi.annotation.Auto;
import kz.kegoc.bln.ejb.cdi.annotation.Oic;
import kz.kegoc.bln.entity.common.*;
import kz.kegoc.bln.entity.data.*;
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
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;


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

				if (header.getLines().size()==0) {
					logger.info("List of points is empty, import data stopped");
					return;
				}

				LocalDateTime startDateTime = buildStartTime();
				LocalDateTime endDateTime = buildEndDateTime();
				if (startDateTime.isAfter(endDateTime)){
					logger.info("Import data is not required, import data stopped");
					return;
				}

				Batch batch = batchHelper.createBatch(new Batch(header, ParamTypeEnum.PT));
				try {
					List<PeriodTimeValueRaw> ptList = oicDataImpGateway
						.points(buildPoints(header.getLines()))
						.startDateTime(startDateTime)
						.endDateTime(endDateTime)
						.request();

					batchHelper.savePtData(batch, ptList);
					batchHelper.updateBatch(batch, null, (long) ptList.size() );
					lastLoadInfoService.pcUpdateLastDate(batch.getId());
					lastLoadInfoService.pcLoad(batch.getId());
				}
				catch (Exception e) {
					logger.error("AutoOicDataReader.read failed: " + e.getMessage());
					batchHelper.updateBatch(batch, e, null);
				}
			});

		logger.info("AutoOicDataReader.read completed");
    }

	private List<String> buildPoints(List<WorkListLine> lines) {
		return Arrays.asList("1", "2");
	}

	private LocalDateTime buildStartTime() {
		LocalDateTime startDateTime = lastLoadInfoService.findAll().stream()
			.filter(l -> l.getSourceSystemCode().equals("OIC"))
			.map(l -> l.getLastLoadDate())
			.max(LocalDateTime::compareTo)
			.orElseGet(null);

		if (startDateTime==null)
			return buildEndDateTime().minusHours(1);

		long step = 180l;
		startDateTime = startDateTime
			.minusSeconds(startDateTime.getMinute()*60 - Math.round(startDateTime.getMinute()*60 / step) * step)
			.plusSeconds(step);

		return startDateTime;
	}

	private LocalDateTime buildEndDateTime() {
		LocalDateTime endDateTime = LocalDateTime.now()
			.truncatedTo(ChronoUnit.MINUTES);

		long step = 180l;
		endDateTime = endDateTime
			.minusSeconds(endDateTime.getMinute()*60 - Math.round(endDateTime.getMinute()*60 / step) * step);

		return endDateTime;
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
