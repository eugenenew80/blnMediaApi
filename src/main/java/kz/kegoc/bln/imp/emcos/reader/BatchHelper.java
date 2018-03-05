package kz.kegoc.bln.imp.emcos.reader;

import kz.kegoc.bln.entity.common.BatchStatusEnum;
import kz.kegoc.bln.entity.common.ParamTypeEnum;
import kz.kegoc.bln.entity.common.SourceSystemEnum;
import kz.kegoc.bln.entity.data.*;
import kz.kegoc.bln.gateway.emcos.MeteringPointCfg;
import kz.kegoc.bln.service.data.BatchService;
import kz.kegoc.bln.service.data.UserTaskHeaderService;
import kz.kegoc.bln.service.data.WorkListHeaderService;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.time.LocalDateTime;

import static kz.kegoc.bln.entity.data.ParamType.newInstance;

@Singleton
public class BatchHelper {

    public Batch startBatch(WorkListHeader header, ParamTypeEnum paramTypeEnum) {
        Batch batch = new Batch();
        batch.setWorkListHeader(header);
        batch.setSourceSystemCode(header.getSourceSystemCode());
        batch.setDirection(header.getDirection());
        batch.setParamType(newInstance(paramTypeEnum));
        batch.setStatus(BatchStatus.newInstance(BatchStatusEnum.P));
        batch.setStartDate(LocalDateTime.now());
        batch = batchService.create(batch);

        header = workListHeaderService.findById(header.getId());
        header.setAtBatch(batch);
        header.setAtStatus(BatchStatus.newInstance(BatchStatusEnum.P));
        workListHeaderService.update(header);
        return batch;
    }

    public Batch startBatch(UserTaskHeader header, ParamTypeEnum paramTypeEnum) {
        Batch batch = new Batch();
        batch.setUserTaskHeader(header);
        batch.setSourceSystemCode(header.getSourceSystemCode());
        batch.setDirection(header.getDirection());
        batch.setParamType(newInstance(paramTypeEnum));
        batch.setStatus(BatchStatus.newInstance(BatchStatusEnum.P));
        batch.setStartDate(LocalDateTime.now());
        batch = batchService.create(batch);

        header = userTaskHeaderService.findById(header.getId());
        header.setAtBatch(batch);
        header.setAtStatus(BatchStatus.newInstance(BatchStatusEnum.P));
        userTaskHeaderService.update(header);
        return batch;
    }


    public Batch endBatch(WorkListHeader header, Batch batch, Long recCount) {
        batch = setBatchToSuccess(batch, recCount);
        setHeaderToSuccess(batch, header);
        return batch;
    }

    public Batch endBatch(UserTaskHeader header, Batch batch, Long recCount) {
        batch = setBatchToSuccess(batch, recCount);
        setHeaderToSuccess(batch, header);
        return batch;
    }


    public Batch errorBatch(WorkListHeader header, Batch batch, Exception e) {
        batch = setBatchToError(batch, e);
        setHeaderToError(batch, header);
        return batch;
    }

    public Batch errorBatch(UserTaskHeader header, Batch batch, Exception e) {
        batch = setBatchToError(batch, e);
        setHeaderToError(batch, header);
        return batch;
    }

    private Batch setBatchToError(Batch batch, Exception e) {
        batch.setStatus(BatchStatus.newInstance(BatchStatusEnum.E));
        batch.setEndDate(LocalDateTime.now());
        batch.setErrMsg(e.getMessage());
        batchService.update(batch);
        return batch;
    }

    private Batch setBatchToSuccess(Batch batch, Long recCount) {
        batch.setStatus(BatchStatus.newInstance(BatchStatusEnum.C));
        batch.setEndDate(LocalDateTime.now());
        batch.setRecCount(recCount);
        batchService.update(batch);
        return batch;
    }

    private WorkListHeader setHeaderToError(Batch batch, WorkListHeader header) {
        header = workListHeaderService.findById(header.getId());
        header.setAtStatus(BatchStatus.newInstance(BatchStatusEnum.E));
        workListHeaderService.update(header);
        return header;
    }

    private UserTaskHeader setHeaderToError(Batch batch, UserTaskHeader header) {
        header = userTaskHeaderService.findById(header.getId());
        header.setAtStatus(BatchStatus.newInstance(BatchStatusEnum.E));
        userTaskHeaderService.update(header);
        return header;
    }

    private WorkListHeader setHeaderToSuccess(Batch batch, WorkListHeader header) {
        header = workListHeaderService.findById(header.getId());
        header.setAtStatus(BatchStatus.newInstance(BatchStatusEnum.C));
        workListHeaderService.update(header);
        return header;
    }

    private UserTaskHeader setHeaderToSuccess(Batch batch, UserTaskHeader header) {
        header = userTaskHeaderService.findById(header.getId());
        header.setAtStatus(BatchStatus.newInstance(BatchStatusEnum.C));
        userTaskHeaderService.update(header);
        return header;
    }


    public MeteringPointCfg buildPointCfg(UserTaskLine line) {
        ParameterConf parameterConf = line.getParam().getConfs()
            .stream()
            .filter(c -> c.getSourceSystemCode().equals(SourceSystem.newInstance(SourceSystemEnum.EMCOS)))
            .findFirst()
            .orElse(null);

        if (parameterConf!=null) {
            MeteringPointCfg mpc = new MeteringPointCfg();
            mpc.setSourceParamCode(parameterConf.getSourceParamCode());
            mpc.setSourceUnitCode(parameterConf.getSourceUnitCode());
            mpc.setInterval(parameterConf.getInterval());
            mpc.setSourceMeteringPointCode(line.getMeteringPoint().getExternalCode());
            mpc.setParamCode(line.getParam().getCode());
            mpc.setStartTime(line.getStartMeteringDate());
            mpc.setEndTime(line.getEndMeteringDate());
            if (!(mpc.getStartTime().isEqual(mpc.getEndTime()) || mpc.getStartTime().isAfter(mpc.getEndTime())))
                return mpc;
        }

        return null;
    }

    public MeteringPointCfg buildPointCfg(WorkListLine line, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        ParameterConf parameterConf = line.getParam().getConfs()
            .stream()
            .filter(c -> c.getSourceSystemCode().equals(SourceSystem.newInstance(SourceSystemEnum.EMCOS)))
            .findFirst()
            .orElse(null);

        if (parameterConf!=null) {
            MeteringPointCfg mpc = new MeteringPointCfg();
            mpc.setSourceMeteringPointCode(line.getMeteringPoint().getExternalCode());
            mpc.setSourceParamCode(parameterConf.getSourceParamCode());
            mpc.setSourceUnitCode(parameterConf.getSourceUnitCode());
            mpc.setInterval(parameterConf.getInterval());
            mpc.setParamCode(line.getParam().getCode());
            mpc.setStartTime(startDateTime);
            mpc.setEndTime(endDateTime);
            if (!(mpc.getStartTime().isEqual(mpc.getEndTime()) || mpc.getStartTime().isAfter(mpc.getEndTime())))
                return mpc;
        }

        return null;
    }


    @Inject
    private WorkListHeaderService workListHeaderService;

    @Inject
    private UserTaskHeaderService userTaskHeaderService;

    @Inject
    private BatchService batchService;
}
