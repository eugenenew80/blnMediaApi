package kz.kegoc.bln.producer.raw.hourly;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.ejb.*;
import javax.ejb.Singleton;
import javax.inject.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.producer.common.EmcosDataRequester;
import kz.kegoc.bln.entity.media.*;
import kz.kegoc.bln.producer.common.MeteringDataProducer;
import kz.kegoc.bln.queue.common.MeteringDataQueueService;

import static kz.kegoc.bln.producer.common.EmcosConfig.defaultEmcosServer;

@Singleton
@Startup
public class EmcosHourlyMeteringDataRawProducer implements MeteringDataProducer {

	@Schedule(second = "*/30", minute = "*", hour = "*", persistent = false)
	public void execute() {
		System.out.println("EmcosHourlyMeteringDataRawProducer started");

		TypedQuery<MeteringPoint> query = em.createNamedQuery("LoadMeteringInfo.findAll", MeteringPoint.class);
		List<MeteringPoint> points = query.getResultList();

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime endDateTime =  LocalDateTime.of(
											now.getYear(),
											now.getMonth(),
											now.getDayOfMonth(),
											now.getHour(),
											Math.round(now.getMinute() / 15)*15
										);

		try {
			List<HourlyMeteringDataRaw> meteringData = new EmcosDataRequester(defaultEmcosServer().build())
					.requestMeteringData(points, endDateTime);

			//List<HourlyMeteringDataRaw> meteringData = new ArrayList<>();
			service.addMeteringListData(meteringData);

			meteringData.stream()
				.map(t -> t.getId())
				.distinct()
				.collect(Collectors.toList())
				.forEach(id -> {
					Date lastLoadedDate = meteringData.stream()
						.map(p -> p.getMeteringDate())
						.max(Date::compareTo).get();

					LoadMeteringInfo l = em.find(LoadMeteringInfo.class, id);
					if (l==null) {
						l = new LoadMeteringInfo();
						l.setId(id);
						l.setLastLoadedDate(lastLoadedDate);
						l.setLastRequestedDate(Date.from(endDateTime.toInstant(ZoneOffset.UTC)));
					}
					em.persist(l);
				});
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("EmcosHourlyMeteringDataRawProducer finished");	
    }


	@Inject
	private MeteringDataQueueService<HourlyMeteringDataRaw> service;

	@PersistenceContext(unitName = "bln")
	private EntityManager em;
}
