package kz.kegoc.bln.imp;

import kz.kegoc.bln.common.enums.BatchStatusEnum;
import kz.kegoc.bln.common.enums.ParamTypeEnum;
import kz.kegoc.bln.common.enums.SourceSystemEnum;
import kz.kegoc.bln.entity.media.*;
import kz.kegoc.bln.gateway.emcos.MeteringPointCfg;
import kz.kegoc.bln.imp.raw.AtTimeValueRaw;
import kz.kegoc.bln.imp.raw.PeriodTimeValueRaw;
import kz.kegoc.bln.service.AtTimeValueRawService;
import kz.kegoc.bln.service.BatchService;
import kz.kegoc.bln.service.PeriodTimeValueRawService;
import kz.kegoc.bln.service.WorkListHeaderService;
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
        atService.saveAll(list);
    }


    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void savePtData(Batch batch, List<PeriodTimeValueRaw> list) {
        list.forEach(t -> t.setBatch(batch));
        ptService.saveAll(list);
    }


    public MeteringPointCfg buildPointCfg(WorkListLine line, LocalDateTime startTime, LocalDateTime endTime, ParamTypeEnum paramType, Integer interval) {
        ParameterConf parameterConf = line.getParam().getConfs()
            .stream()
            .filter(c -> c.getSourceSystemCode().equals(SourceSystem.newInstance(SourceSystemEnum.EMCOS)))
            .filter(c -> c.getParamType().equals(ParamType.newInstance(paramType)))
            .filter(c -> (c.getInterval()==null && interval==null) || c.getInterval().equals(interval))
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


    public LastLoadInfo getLastLoadIfo(List<LastLoadInfo> lastLoadInfoList, WorkListLine line, ParamTypeEnum paramType, Integer interval) {
        ParameterConf parameterConf = line.getParam().getConfs()
            .stream()
            .filter(c -> c.getSourceSystemCode().equals(SourceSystem.newInstance(SourceSystemEnum.EMCOS)))
            .filter(c -> c.getParamType().equals( ParamType.newInstance(paramType)) )
            .filter(c ->  (c.getInterval()==null && interval==null) || c.getInterval().equals(interval))
            .findFirst()
            .orElse(null);

        if (parameterConf==null) {
            System.out.println(interval);
            System.out.println(paramType);
            return null;
        }

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
    private AtTimeValueRawService atService;

    @Inject
    private PeriodTimeValueRawService ptService;
}
