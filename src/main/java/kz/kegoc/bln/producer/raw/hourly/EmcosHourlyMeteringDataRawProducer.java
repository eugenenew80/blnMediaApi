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
import org.apache.commons.lang3.tuple.Pair;
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

		List<MeteringPoint> points = em.createNamedQuery("LoadMeteringInfo.findAll", MeteringPoint.class)
				.getResultList();

		LocalDateTime now = LocalDateTime.now().minusMinutes(15);
		LocalDateTime endDateTime =  LocalDateTime.of(
				now.getYear(),
				now.getMonth(),
				now.getDayOfMonth(),
				now.getHour(),
				Math.round(now.getMinute() / 15)*15
			).plusHours(1);

		try {
			List<HourlyMeteringDataRaw> minuteData = new EmcosDataRequester(defaultEmcosServer().build())
					.requestMeteringData(points, endDateTime);

			List<HourlyMeteringDataRaw> hourData = new ArrayList<>();
			
			Map<Pair<String, LocalDateTime>, List<HourlyMeteringDataRaw>> mapPairs = minuteData.stream()
					.collect( Collectors.groupingBy( m -> Pair.of(m.getExternalCode(), m.getMeteringDate()) ) );

			for ( Pair<String, LocalDateTime> pair : mapPairs.keySet() ) {
				HourlyMeteringDataRaw h = new HourlyMeteringDataRaw();
				
				h.setStatus(mapPairs.get(pair).get(0).getStatus());
				h.setExternalCode(mapPairs.get(pair).get(0).getExternalCode());
				h.setMeteringDate(mapPairs.get(pair).get(0).getMeteringDate());
				h.setDataSourceCode(mapPairs.get(pair).get(0).getDataSourceCode());
				h.setParamCode(mapPairs.get(pair).get(0).getParamCode());
				h.setUnitCode(mapPairs.get(pair).get(0).getUnitCode());
				h.setWayEntering(mapPairs.get(pair).get(0).getWayEntering());
				h.setHour(mapPairs.get(pair).get(0).getHour());
				
				h.setVal(mapPairs.get(pair).stream().mapToDouble(m -> m.getVal()).sum());
				hourData.add(h);
			}
			
			service.addMeteringListData(hourData);

			hourData.stream()
				.map(t -> t.getExternalCode())
				.distinct()
				.collect(Collectors.toList())
				.forEach(externalCode -> {

					LocalDateTime lastLoadedDate = hourData.stream()
						.map(p -> p.getMeteringDate())
						.max(LocalDateTime::compareTo).get();

					MeteringPoint p  = em.createNamedQuery("MeteringPoint.findByExternalCode", MeteringPoint.class)
						.setParameter("externalCode", externalCode)
						.getResultList()
						.stream()
						.findFirst()
						.orElse(null);

					if (p!=null ) {
						LoadMeteringInfo l = em.find(LoadMeteringInfo.class, p.getId());
						if (l==null) 
							l = new LoadMeteringInfo();
						
						l.setId(p.getId());
						l.setLastLoadedDate(lastLoadedDate);
						l.setLastRequestedDate(endDateTime);
						
						em.persist(l);
					}
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
