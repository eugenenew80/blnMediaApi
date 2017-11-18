package kz.kegoc.bln.updater.impl;

import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.raw.DayMeteringBalanceRaw;
import kz.kegoc.bln.repository.common.query.ConditionType;
import kz.kegoc.bln.repository.common.query.MyQueryParam;
import kz.kegoc.bln.repository.common.query.Query;
import kz.kegoc.bln.repository.common.query.QueryImpl;
import kz.kegoc.bln.service.dict.MeteringPointService;
import kz.kegoc.bln.service.media.raw.MeteringDataRawService;
import kz.kegoc.bln.updater.MeteringDataUpdater;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;


@Singleton
public class DayMeteringBalanceRawUpdater implements MeteringDataUpdater<DayMeteringBalanceRaw> {

    @Schedule(minute = "*/5", hour = "*", persistent = false)
    public void update() {
        Query query = QueryImpl.builder()
            .setParameter("status", new MyQueryParam("status", DataStatus.RAW, ConditionType.EQUALS))
            .setOrderBy("t.id")
            .build();

        service.find(query).stream()
            .forEach(t -> {
                MeteringPoint meteringPoint = meteringPointService.findByExternalCode(t.getExternalCode());
                if (meteringPoint!=null) {
                    t.setStatus(DataStatus.OK);
                    t.setMeteringPoint(meteringPoint);
                    if (meteringPoint.getMeters().size()>0)
                        t.setMeter(meteringPoint.getMeters().get(0).getMeter());
                }
                else
                    t.setStatus(DataStatus.ERROR);

                service.update(t);
            });
    }

    @Inject
    private MeteringDataRawService<DayMeteringBalanceRaw> service;

    @Inject
    private MeteringPointService meteringPointService;
}
