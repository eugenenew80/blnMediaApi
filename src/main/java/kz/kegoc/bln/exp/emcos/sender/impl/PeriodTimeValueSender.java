package kz.kegoc.bln.exp.emcos.sender.impl;

import kz.kegoc.bln.entity.data.Batch;
import kz.kegoc.bln.entity.data.ExportData;
import kz.kegoc.bln.entity.data.PeriodTimeValue;
import kz.kegoc.bln.entity.data.WorkListHeader;
import kz.kegoc.bln.exp.emcos.sender.Sender;
import kz.kegoc.bln.gateway.ftp.FtpGateway;
import kz.kegoc.bln.service.data.BatchService;
import kz.kegoc.bln.service.data.WorkListHeaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class PeriodTimeValueSender implements Sender<PeriodTimeValue> {
    private static final Logger logger = LoggerFactory.getLogger(PeriodTimeValueSender.class);
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'_'HHmmss");

    public void send() {
        Query query = em.createNamedQuery("ExportData.findExportData", ExportData.class);
        headerService.findAll().stream()
            .filter(h -> h.getActive() && h.getStatus().equals("W") &&  h.getSourceSystemCode().equals("EMCOS") && h.getDirection().equals("EXPORT"))
            .forEach(header -> {
                Map<String, List<ExportData>> exportData = new HashMap<>();
                String path = header.getDirection();
                String fileName = "BIS_" + header.getId() + "_" +  LocalDateTime.now().format(timeFormatter);
                Batch batch = startBatch(header);

                try {
                    header.getLines().stream()
                        .forEach(line -> {
                            query.setParameter("sourceMeteringPointCode", line.getMeteringPoint().getExternalCode());
                            query.setParameter("startDate", header.getStartDate());
                            query.setParameter("endDate", header.getEndDate());
                            exportData.put(line.getMeteringPoint().getExternalCode(), query.getResultList());
                        });

                    Long recCount = 0l;
                    for (String key : exportData.keySet())
                        recCount += exportData.get(key).size();

                    ftpGateway
                        .config(header.getConfig())
                        .exportData(exportData)
                        .path(path)
                        .fileName(fileName)
                        .send();

                    endBatch(header, batch, recCount);
                }

                catch (Exception e) {
                    logger.error("PeriodTimeValueSender.send failed: " + e.getMessage());
                    errorBatch(header, batch, e);
                }
            });
    }


    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private Batch startBatch(WorkListHeader header) {
        Batch batch = new Batch();
        batch.setWorkListHeader(header);
        batch.setSourceSystemCode(header.getSourceSystemCode());
        batch.setDirection(header.getDirection());
        batch.setParamType("PT");
        batch.setStatus("P");
        batch.setStartDate(LocalDateTime.now());
        batchService.create(batch);

        header = headerService.findById(header.getId());
        header.setBatch(batch);
        header.setStatus("P");
        headerService.update(header);
        return batch;
    }


    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private Batch endBatch(WorkListHeader header, Batch batch, Long retCount) {
        batch.setStatus("C");
        batch.setEndDate(LocalDateTime.now());
        batch.setRecCount(retCount);
        batchService.update(batch);

        header = headerService.findById(header.getId());
        header.setStatus("C");
        headerService.update(header);
        return batch;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private Batch errorBatch(WorkListHeader header, Batch batch, Exception e) {
        batch.setStatus("E");
        batch.setEndDate(LocalDateTime.now());
        batch.setErrMsg(e.getMessage());
        batchService.update(batch);

        header = headerService.findById(header.getId());
        header.setStatus("E");
        headerService.update(header);
        return batch;
    }


    @Inject
    private FtpGateway ftpGateway;

    @Inject
    private WorkListHeaderService headerService;

    @Inject
    private BatchService batchService;

    @PersistenceContext(unitName = "bln")
    private EntityManager em;
}
