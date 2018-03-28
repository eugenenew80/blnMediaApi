package kz.kegoc.bln.gateway.oic.impl;

import kz.kegoc.bln.imp.raw.TelemetryRaw;
import kz.kegoc.bln.gateway.oic.OicDataImpGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Singleton
public class OicDataImpGatewayImpl implements OicDataImpGateway {
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final Logger logger = LoggerFactory.getLogger(OicDataImpGatewayImpl.class);

    @Override
    public List<TelemetryRaw> request() throws Exception {
        LocalDateTime start = LocalDateTime.of(2018, 3, 28, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2018, 4, 20, 0, 5, 0);

        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target("http://10.8.0.76:8081");
        WebTarget telemetryWebTarget = webTarget.path("exp/telemetry")
            .queryParam("start", start.format(timeFormatter))
            .queryParam("end", end.format(timeFormatter))
            .queryParam("arcType", "MIN-3");

        Invocation.Builder invocationBuilder = telemetryWebTarget.request(MediaType.APPLICATION_JSON);
        List<TelemetryRaw> response = invocationBuilder.get((new GenericType<List<TelemetryRaw>>(){}));

        return response;
    }
}
