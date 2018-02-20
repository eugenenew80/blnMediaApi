package kz.kegoc.bln.gateway.ftp;

import java.util.List;

public interface FtpGateway {
    FtpGateway pcList(List<ExportPoint> pcList);
    void send();
}
