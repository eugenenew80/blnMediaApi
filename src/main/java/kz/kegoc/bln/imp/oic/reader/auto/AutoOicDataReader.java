package kz.kegoc.bln.imp.oic.reader.auto;

import kz.kegoc.bln.ejb.annotation.Auto;
import kz.kegoc.bln.ejb.annotation.Oic;
import kz.kegoc.bln.entity.media.*;
import kz.kegoc.bln.common.enums.DirectionEnum;
import kz.kegoc.bln.common.enums.ParamTypeEnum;
import kz.kegoc.bln.common.enums.SourceSystemEnum;
import kz.kegoc.bln.gateway.oic.OicDataImpGateway;
import kz.kegoc.bln.imp.BatchHelper;
import kz.kegoc.bln.imp.Reader;
import kz.kegoc.bln.imp.raw.PeriodTimeValueRaw;
import kz.kegoc.bln.imp.raw.TelemetryRaw;
import kz.kegoc.bln.service.LastLoadInfoService;
import kz.kegoc.bln.service.WorkListHeaderService;
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
		logger.info("read started");

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
					logger.info("List of points is empty, import media stopped");
					return;
				}

				LocalDateTime startDateTime = buildStartTime();
				LocalDateTime endDateTime = buildEndDateTime();
				if (startDateTime.isAfter(endDateTime)){
					logger.info("Import media is not required, import media stopped");
					return;
				}

				Batch batch = batchHelper.createBatch(new Batch(header, ParamTypeEnum.PT));
				try {
					List<PeriodTimeValueRaw> ptList = oicDataImpGateway
						.points(buildPoints(header.getLines()))
						.startDateTime(startDateTime)
						.endDateTime(endDateTime)
						.arcType("MIN-3")
						.request();

					batchHelper.savePtData(batch, ptList);
					batchHelper.updateBatch(batch, null, (long) ptList.size() );
					lastLoadInfoService.ptUpdateLastDate(batch.getId());
					lastLoadInfoService.ptLoad(batch.getId());
				}
				catch (Exception e) {
					logger.error("read failed: " + e.getMessage());
					batchHelper.updateBatch(batch, e, null);
				}
			});

		logger.info("read completed");
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
