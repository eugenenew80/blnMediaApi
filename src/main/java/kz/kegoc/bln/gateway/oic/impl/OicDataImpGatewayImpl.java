package kz.kegoc.bln.gateway.oic.impl;

import kz.kegoc.bln.common.enums.InputMethodEnum;
import kz.kegoc.bln.common.enums.ProcessingStatusEnum;
import kz.kegoc.bln.common.enums.ReceivingMethodEnum;
import kz.kegoc.bln.common.enums.SourceSystemEnum;
import kz.kegoc.bln.entity.media.InputMethod;
import kz.kegoc.bln.entity.media.ReceivingMethod;
import kz.kegoc.bln.imp.raw.PeriodTimeValueRaw;
import kz.kegoc.bln.imp.raw.TelemetryRaw;
import kz.kegoc.bln.gateway.oic.OicDataImpGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Singleton
public class OicDataImpGatewayImpl implements OicDataImpGateway {
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final Logger logger = LoggerFactory.getLogger(OicDataImpGatewayImpl.class);

    @Override
    public OicDataImpGateway points(List<String> points) {
        this.points = points;
        return this;
    }

    @Override
    public OicDataImpGateway startDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
        return this;
    }

    @Override
    public OicDataImpGateway endDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
        return this;
    }

    @Override
    public OicDataImpGateway arcType(String arcType) {
        this.arcType = arcType;
        return this;
    }

    @Override
    public List<PeriodTimeValueRaw> request() throws Exception {
        logger.info(startDateTime.toString());
        logger.info(endDateTime.toString());

        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://10.8.0.76:8081");
        WebTarget telemetryWebTarget = webTarget.path("rest/exp/telemetry")
            .queryParam("start", startDateTime.format(timeFormatter))
            .queryParam("end", endDateTime.format(timeFormatter))
            .queryParam("arcType", arcType);

        List<TelemetryRaw> response =
            telemetryWebTarget.request(MediaType.APPLICATION_JSON)
            .get((new GenericType<List<TelemetryRaw>>(){}));

        return mapToPeriodTimeValue(response);
    }


    private List<PeriodTimeValueRaw> mapToPeriodTimeValue(List<TelemetryRaw> teleiemetries) {
        return teleiemetries.stream()
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
                return pt;
            })
            .collect(toList());
    }

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String arcType;
    private List<String> points;
}
