package kz.kegoc.bln.gateway.ftp;

import kz.kegoc.bln.entity.data.PowerConsumption;

import java.util.List;

public interface FtpGateway {
    FtpGateway pcList(List<PowerConsumption> pcList);
    void send();
}
