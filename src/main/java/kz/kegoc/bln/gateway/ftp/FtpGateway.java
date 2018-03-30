package kz.kegoc.bln.gateway.ftp;

import kz.kegoc.bln.entity.media.ConnectionConfig;
import kz.kegoc.bln.entity.media.ExportData;

import java.util.List;
import java.util.Map;

public interface FtpGateway {
    FtpGateway exportData(Map<String, List<ExportData>> exportData);
    FtpGateway path(String path);
    FtpGateway fileName(String fileName);
    FtpGateway config(ConnectionConfig config);
    void send() throws Exception;
}
