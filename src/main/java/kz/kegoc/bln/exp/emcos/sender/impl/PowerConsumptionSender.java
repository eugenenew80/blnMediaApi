package kz.kegoc.bln.exp.emcos.sender.impl;

import kz.kegoc.bln.entity.data.PowerConsumption;
import kz.kegoc.bln.exp.emcos.sender.Sender;
import kz.kegoc.bln.gateway.ftp.ExportPoint;
import kz.kegoc.bln.gateway.ftp.FtpGateway;
import kz.kegoc.bln.service.data.PowerConsumptionService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class PowerConsumptionSender implements Sender<PowerConsumption> {

    public void send() {
        String sourceMeteringPointCode = "113440990999999999";
        LocalDateTime startDate = LocalDate.of(2017, Month.FEBRUARY, 1).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(2017, Month.FEBRUARY, 1).atStartOfDay().plusHours(23);

        List<ExportPoint> exportList = new ArrayList<>();

        ExportPoint exportPoint = new ExportPoint();
        List<PowerConsumption> list = pcService.findByExternalCode(sourceMeteringPointCode,  startDate, endDate);
        exportPoint.setSourceMeteringPointCode(sourceMeteringPointCode);
        exportPoint.setData(list);
        exportList.add(exportPoint);

        ftpGateway
            .pcList(exportList)
            .send();
    }

    @Inject
    private FtpGateway ftpGateway;

    @Inject
    private PowerConsumptionService pcService;
}
