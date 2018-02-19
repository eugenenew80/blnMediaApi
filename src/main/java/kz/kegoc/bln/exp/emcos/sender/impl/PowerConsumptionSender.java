package kz.kegoc.bln.exp.emcos.sender.impl;

import kz.kegoc.bln.entity.data.PowerConsumption;
import kz.kegoc.bln.exp.emcos.sender.Sender;
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
        String externalCode = "121450213010010007";
        LocalDateTime startDate = LocalDate.of(2018, Month.FEBRUARY, 18).atStartOfDay();
        LocalDateTime endDate = LocalDate.of(2018, Month.FEBRUARY, 18).atStartOfDay().plusHours(23);
        List<PowerConsumption> pcList = pcService.findByExternalCode(externalCode, startDate, endDate);

        ftpGateway
            .pcList(pcList)
            .send();
    }

    @Inject
    private FtpGateway ftpGateway;

    @Inject
    private PowerConsumptionService pcService;
}
