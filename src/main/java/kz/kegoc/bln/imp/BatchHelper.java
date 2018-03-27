package kz.kegoc.bln.imp;

import kz.kegoc.bln.entity.common.BatchStatusEnum;
import kz.kegoc.bln.entity.common.ParamTypeEnum;
import kz.kegoc.bln.entity.common.SourceSystemEnum;
import kz.kegoc.bln.entity.data.*;
import kz.kegoc.bln.gateway.emcos.MeteringPointCfg;
import kz.kegoc.bln.service.data.BatchService;
import kz.kegoc.bln.service.data.MeteringValueService;
import kz.kegoc.bln.service.data.WorkListHeaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.*;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import static javax.ejb.LockType.READ;
import static javax.ejb.LockType.WRITE;

@Singleton
@Lock(READ)
public class BatchHelper {
    private static final Logger logger = LoggerFactory.getLogger(BatchHelper.class);

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Batch createBatch(Batch batch) {
        batch = batchService.create(batch);
        updateHeader(batch, batch.getWorkListHeader());
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
        return batch;
    }


    @Lock(WRITE)
    @AccessTimeout(value=60000)
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


    public MeteringPointCfg buildPointCfg(WorkListLine line, LocalDateTime startTime, LocalDateTime endTime) {
        ParameterConf parameterConf = line.getParam().getConfs()
            .stream()
            .filter(c -> c.getSourceSystemCode().equals(SourceSystem.newInstance(SourceSystemEnum.EMCOS)))
            .findFirst()
            .orElse(null);

        if (parameterConf==null) return null;

        MeteringPointCfg mpc = new MeteringPointCfg();
        mpc.setSourceParamCode(parameterConf.getSourceParamCode());
        mpc.setSourceUnitCode(parameterConf.getSourceUnitCode());
        mpc.setInterval(parameterConf.getInterval());
        mpc.setSourceMeteringPointCode(line.getMeteringPoint().getExternalCode());
        mpc.setParamCode(line.getParam().getCode());
        mpc.setStartTime(startTime);
        mpc.setEndTime(endTime);

        return mpc;
    }


    public LastLoadInfo getLastLoadIfo(List<LastLoadInfo> lastLoadInfoList, WorkListLine line) {
        ParameterConf parameterConf = line.getParam().getConfs()
            .stream()
            .filter(c -> c.getSourceSystemCode().equals(SourceSystem.newInstance(SourceSystemEnum.EMCOS)))
            .findFirst()
            .orElse(null);

        if (parameterConf==null) return null;

        LastLoadInfo lastLoadInfo = lastLoadInfoList.stream()
            .filter(t -> t.getSourceMeteringPointCode().equals(line.getMeteringPoint().getExternalCode()) && t.getSourceParamCode().equals(parameterConf.getSourceParamCode()))
            .findFirst()
            .orElse(null);

        return lastLoadInfo;
    }


    @Inject
    private WorkListHeaderService workListHeaderService;

    @Inject
    private BatchService batchService;

    @Inject
    private MeteringValueService<AtTimeValueRaw> mrService;

    @Inject
    private MeteringValueService<PeriodTimeValueRaw> pcService;
}
