package kz.kegoc.bln.imp.emcos.reader;

import kz.kegoc.bln.entity.common.BatchStatusEnum;
import kz.kegoc.bln.entity.common.ParamTypeEnum;
import kz.kegoc.bln.entity.data.*;
import kz.kegoc.bln.service.data.BatchService;
import kz.kegoc.bln.service.data.MeteringValueService;
import kz.kegoc.bln.service.data.UserTaskHeaderService;
import kz.kegoc.bln.service.data.WorkListHeaderService;
import javax.ejb.*;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import static javax.ejb.LockType.WRITE;

@Singleton
@Lock(WRITE)
@AccessTimeout(value=60000)
public class BatchHelper {

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Batch createBatch(Batch batch) {
        batch = batchService.create(batch);
        updateHeader(batch, batch.getWorkListHeader());
        updateHeader(batch, batch.getUserTaskHeader());
        return batch;
    }


    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Batch updateBatch(Batch batch, Exception e, Long recCount) {
        batch.setEndDate(LocalDateTime.now());
        if (e!=null) {
            batch.setStatus(BatchStatus.newInstance(BatchStatusEnum.E));
            batch.setErrMsg(e.getMessage());
        }
        else {
            batch.setStatus(BatchStatus.newInstance(BatchStatusEnum.C));
            batch.setRecCount(recCount);
        }

        batch = batchService.update(batch);
        updateHeader(batch, batch.getWorkListHeader());
        updateHeader(batch, batch.getUserTaskHeader());
        return batch;
    }


    private WorkListHeader updateHeader(Batch batch, WorkListHeader header) {
        if (header==null) return null;

        header = workListHeaderService.findById(header.getId());
        if (ParamType.newInstance(ParamTypeEnum.AT).equals(batch.getParamType())) {
            header.setAtBatch(batch);
            header.setAtStatus(batch.getStatus());
        }

        if (ParamType.newInstance(ParamTypeEnum.PT).equals(batch.getParamType())) {
            header.setPtBatch(batch);
            header.setPtStatus(batch.getStatus());
        }

        workListHeaderService.update(header);
        return header;
    }

    private UserTaskHeader updateHeader(Batch batch, UserTaskHeader header) {
        if (header==null) return null;

        header = userTaskHeaderService.findById(header.getId());
        if (ParamType.newInstance(ParamTypeEnum.AT).equals(batch.getParamType())) {
            header.setAtBatch(batch);
            header.setAtStatus(batch.getStatus());
        }

        if (ParamType.newInstance(ParamTypeEnum.PT).equals(batch.getParamType())) {
            header.setPtBatch(batch);
            header.setPtStatus(batch.getStatus());
        }

        userTaskHeaderService.update(header);
        return header;
    }


    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void saveAtData(Batch batch, List<AtTimeValueRaw> list) {
        list.forEach(t -> t.setBatch(batch));
        mrService.saveAll(list);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void savePtData(Batch batch, List<PeriodTimeValueRaw> list) {
        list.forEach(t -> t.setBatch(batch));
        pcService.saveAll(list);
    }



    @Inject
    private WorkListHeaderService workListHeaderService;

    @Inject
    private UserTaskHeaderService userTaskHeaderService;

    @Inject
    private BatchService batchService;

    @Inject
    private MeteringValueService<AtTimeValueRaw> mrService;

    @Inject
    private MeteringValueService<PeriodTimeValueRaw> pcService;
}
