package kz.kegoc.bln.gateway.oic.impl;

import kz.kegoc.bln.entity.data.Telemetry;
import kz.kegoc.bln.gateway.oic.OicDataImpGateway;
import kz.kegoc.bln.imp.emcos.schedule.AutoAtTimeValueImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OicDataImpGatewayImpl implements OicDataImpGateway {
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final Logger logger = LoggerFactory.getLogger(OicDataImpGatewayImpl.class);

    @Override
    public void request() throws Exception {
        LocalDateTime start = LocalDateTime.of(2018, 3, 20, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2018, 3, 20, 15, 5, 0);

        Client client = ClientBuilder.newClient();

        WebTarget webTarget = client.target("http://localhost:8081");
        WebTarget telemetryWebTarget = webTarget.path("telemetry")
            .queryParam("start", start.format(timeFormatter))
            .queryParam("end", end.format(timeFormatter));

        Invocation.Builder invocationBuilder = telemetryWebTarget.request(MediaType.APPLICATION_JSON);
        List<Telemetry> response = invocationBuilder.get((new GenericType<List<Telemetry>>(){}));

        response.stream().forEach(l -> logger.info(l.toString()));
    }
}
