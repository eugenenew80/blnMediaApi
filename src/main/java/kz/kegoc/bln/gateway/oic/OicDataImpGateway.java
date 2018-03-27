package kz.kegoc.bln.gateway.oic;

import kz.kegoc.bln.imp.raw.TelemetryRaw;

import javax.ejb.Local;
import java.util.List;

@Local
public interface OicDataImpGateway {
    List<TelemetryRaw> request() throws Exception;
}
