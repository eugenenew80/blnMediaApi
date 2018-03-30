package kz.kegoc.bln.gateway.oic;

import kz.kegoc.bln.imp.raw.PeriodTimeValueRaw;

import javax.ejb.Local;
import java.time.LocalDateTime;
import java.util.List;

@Local
public interface OicDataImpGateway {
    OicDataImpGateway points(List<String> points);

    OicDataImpGateway startDateTime(LocalDateTime startDateTime);

    OicDataImpGateway endDateTime(LocalDateTime endDateTime);

    OicDataImpGateway arcType(String arcType);

    List<PeriodTimeValueRaw> request() throws Exception;
}
