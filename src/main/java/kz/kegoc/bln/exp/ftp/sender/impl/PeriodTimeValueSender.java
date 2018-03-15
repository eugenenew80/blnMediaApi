package kz.kegoc.bln.exp.ftp.sender.impl;

import kz.kegoc.bln.entity.common.BatchStatusEnum;
import kz.kegoc.bln.entity.common.DirectionEnum;
import kz.kegoc.bln.entity.common.ParamTypeEnum;
import kz.kegoc.bln.entity.common.SourceSystemEnum;
import kz.kegoc.bln.entity.data.*;
import kz.kegoc.bln.exp.ftp.sender.Sender;
import kz.kegoc.bln.gateway.ftp.FtpGateway;
import kz.kegoc.bln.service.data.BatchService;
import kz.kegoc.bln.service.data.WorkListHeaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static kz.kegoc.bln.entity.data.ParamType.newInstance;

@Stateless
@Default
public class PeriodTimeValueSender implements Sender<PeriodTimeValue> {
    private static final Logger logger = LoggerFactory.getLogger(PeriodTimeValueSender.class);
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'_'HHmmss");

    public void send() {
        AtomicBoolean flag = new AtomicBoolean(false);

        Query query = em.createNamedQuery("ExportData.findExportData", ExportData.class);
        headerService.findAll().stream()
            .filter(h -> h.getActive()
                && (BatchStatus.newInstance(BatchStatusEnum.W).equals(h.getPtStatus()))
                && SourceSystem.newInstance(SourceSystemEnum.EMCOS).equals(h.getSourceSystemCode())
                && Direction.newInstance(DirectionEnum.EXPORT).equals(h.getDirection())
                && h.getConfig()!=null
            )
            .forEach(header -> {
                if (!flag.get())
                    logger.warn("PeriodTimeValueSender.send started");

                flag.set(true);

                if (header.getLines().size()==0) {
                    logger.warn("List of points is empty, import data stopped");
                    return;
                }

                if (header.getStartDate()==null || header.getEndDate()==null) {
                    logger.warn("Period must be specified");
                    return;
                }

                Map<String, List<ExportData>> exportData = new HashMap<>();
                String path = "/home/bis-user/export";
                String fileName = "BIS_" + header.getId() + "_" +  LocalDateTime.now().format(timeFormatter);
                Batch batch = startBatch(header);

                try {
                    header.getLines().stream()
                        .forEach(line -> {
                            logger.info("find data for export: " + line.getMeteringPoint().getExternalCode());
                            query.setParameter("sourceMeteringPointCode", line.getMeteringPoint().getExternalCode());
                            query.setParameter("startDate", header.getStartDate());
                            query.setParameter("endDate", header.getEndDate());
                            exportData.put(line.getMeteringPoint().getExternalCode(), query.getResultList());
                        });

                    Long recCount = 0l;
                    for (String key : exportData.keySet())
                        recCount += exportData.get(key).size();

                    if (recCount==0) {
                        logger.info("No data found, export data stopped");
                        return;
                    }

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

        if (flag.get())
            logger.info("PeriodTimeValueSender.send completed");
    }


    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private Batch startBatch(WorkListHeader header) {
        Batch batch = new Batch();
        batch.setWorkListHeader(header);
        batch.setSourceSystemCode(header.getSourceSystemCode());
        batch.setDirection(header.getDirection());
        batch.setParamType(newInstance(ParamTypeEnum.PT));
        batch.setStatus(BatchStatus.newInstance(BatchStatusEnum.P));
        batch.setStartDate(LocalDateTime.now());
        batch = batchService.create(batch);

        header = headerService.findById(header.getId());
        header.setPtBatch(batch);
        header.setPtStatus(BatchStatus.newInstance(BatchStatusEnum.P));
        headerService.update(header);
        return batch;
    }


    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private Batch endBatch(WorkListHeader header, Batch batch, Long retCount) {
        batch.setStatus(BatchStatus.newInstance(BatchStatusEnum.C));
        batch.setEndDate(LocalDateTime.now());
        batch.setRecCount(retCount);
        batchService.update(batch);

        header = headerService.findById(header.getId());
        header.setPtStatus(BatchStatus.newInstance(BatchStatusEnum.C));
        headerService.update(header);
        return batch;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private Batch errorBatch(WorkListHeader header, Batch batch, Exception e) {
        batch.setStatus(BatchStatus.newInstance(BatchStatusEnum.C));
        batch.setEndDate(LocalDateTime.now());
        batch.setErrMsg(e.getMessage());
        batchService.update(batch);

        header = headerService.findById(header.getId());
        header.setPtStatus(BatchStatus.newInstance(BatchStatusEnum.E));
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
