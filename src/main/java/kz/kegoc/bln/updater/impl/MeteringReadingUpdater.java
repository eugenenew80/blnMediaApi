package kz.kegoc.bln.updater.impl;

import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.entity.common.DataStatus;
import kz.kegoc.bln.entity.data.MeteringReading;
import kz.kegoc.bln.repository.common.query.ConditionType;
import kz.kegoc.bln.repository.common.query.MyQueryParam;
import kz.kegoc.bln.repository.common.query.Query;
import kz.kegoc.bln.repository.common.query.QueryImpl;
import kz.kegoc.bln.service.dict.MeteringPointService;
import kz.kegoc.bln.service.data.MeteringDataService;
import kz.kegoc.bln.updater.MeteringDataUpdater;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;


@Singleton
public class MeteringReadingUpdater implements MeteringDataUpdater<MeteringReading> {

    @Schedule(minute = "*/5", hour = "*", persistent = false)
    public void update() {
        Query query = QueryImpl.builder()
            .setParameter("status", new MyQueryParam("status", DataStatus.RAW, ConditionType.EQUALS))
            .setOrderBy("t.id")
            .build();

        service.find(query).stream()
            .forEach(t -> {
                MeteringPoint meteringPoint = meteringPointService.findByExternalCode(t.getSourceMeteringPointCode());
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
    private MeteringDataService<MeteringReading> service;

    @Inject
    private MeteringPointService meteringPointService;
}
