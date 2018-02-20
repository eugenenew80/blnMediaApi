package kz.kegoc.bln.exp.emcos.sender.impl;

import kz.kegoc.bln.entity.data.PowerConsumption;
import kz.kegoc.bln.exp.emcos.sender.Sender;
import kz.kegoc.bln.gateway.ftp.ExportPoint;
import kz.kegoc.bln.gateway.ftp.FtpGateway;
import kz.kegoc.bln.service.data.PowerConsumptionService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

        String sql = "select\n" +
                "\tsource_metering_point_code,\n" +
                "\tpc.metering_date,\n" +
                "\tsum(val_ap) val_ap,\n" +
                "\tsum(val_am) val_ap\n" +
                "from\n" +
                "\t(\n" +
                "\t\tselect\n" +
                "\t\t    pc.source_metering_point_code,\n" +
                "\t\t    pc.metering_date,\n" +
                "\t\t    \n" +
                "\t\t    case\n" +
                "\t\t    \twhen pc.source_param_code = '709' then pc.val\n" +
                "\t\t    \telse null\n" +
                "\t\t    end val_ap,\n" +
                "\n" +
                "\t\t    case\n" +
                "\t\t    \twhen pc.source_param_code = '719' then pc.val\n" +
                "\t\t    \telse null\n" +
                "\t\t    end val_ar\n" +
                "\t\tfrom\n" +
                "\t\t\tapps.media_power_consumptions pc\n" +
                "\t\twhere\n" +
                "\t\t\t    pc.source_metering_point_code = :source_metering_point_code\n" +
                "\t\t\tand\tpc.metering_date between :start_metering_date and :end_metering_date\n" +
                "\t\t\tand pc.source_param_code in ('709', '710')\n" +
                "\t\t\tand pc.interval = 3600\n" +
                "\t)\t\n" +
                "group by\n" +
                "\tsource_metering_point_code,\n" +
                "\tpc.metering_date ";

        Query query = em.createNativeQuery(sql);
        query.setParameter("source_metering_point_code", sourceMeteringPointCode);
        query.setParameter("start_metering_date", startDate);
        query.setParameter("end_metering_date", endDate);

        List<ExportPoint> exportList = new ArrayList<>();
        List<Object[]> list = query.getResultList();
        for (Object[] object : list) {
            ExportPoint exportPoint = new ExportPoint();

            exportPoint.setSourceMeteringPointCode(object[0].toString());
            exportPoint.setValAm(Double.parseDouble(object[2].toString()));
            exportPoint.setValAp(Double.parseDouble(object[3].toString()));

            exportList.add(exportPoint);
        }

        /*
        List<ExportPoint> exportList = new ArrayList<>();

        ExportPoint exportPoint = new ExportPoint();
        List<PowerConsumption> list = pcService.findByExternalCode(sourceMeteringPointCode,  startDate, endDate);
        exportPoint.setSourceMeteringPointCode(sourceMeteringPointCode);
        exportPoint.setData(list);
        exportList.add(exportPoint);
        */

        ftpGateway
            .pcList(exportList)
            .send();

    }

    @Inject
    private FtpGateway ftpGateway;

    @Inject
    private PowerConsumptionService pcService;

    @PersistenceContext(unitName = "bln")
    private EntityManager em;
}
