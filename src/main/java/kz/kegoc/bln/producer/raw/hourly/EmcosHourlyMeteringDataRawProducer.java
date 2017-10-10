package kz.kegoc.bln.producer.raw.hourly;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.ejb.*;
import javax.ejb.Singleton;
import javax.inject.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.tuple.Pair;
import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.producer.common.EmcosDataRequester;
import kz.kegoc.bln.entity.media.*;
import kz.kegoc.bln.producer.common.MeteringDataProducer;
import kz.kegoc.bln.queue.common.MeteringDataQueueService;
import kz.kegoc.bln.service.dict.MeteringPointService;

import static kz.kegoc.bln.producer.common.EmcosConfig.defaultEmcosServer;


@Singleton
@Startup
public class EmcosHourlyMeteringDataRawProducer implements MeteringDataProducer {
	//private boolean starting = true;
		
	@Schedule(minute = "*/15", hour = "*", persistent = false)
	public void execute() {
		//if (!starting)return;
		//starting=false;
		
		System.out.println("EmcosHourlyMeteringDataRawProducer started");

		LocalDateTime endDateTime =  buildEndDateTime();
		List<MeteringPoint> points = meteringPointService.findAll();
		try {			
			List<HourlyMeteringDataRaw> meteringData = new EmcosDataRequester(defaultEmcosServer().build()).requestMeteringData(points, endDateTime);
			service.addMeteringListData(groupMeteringDataByDateTime(meteringData));
			updateLoadMeteringInfo(meteringData, endDateTime);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("EmcosHourlyMeteringDataRawProducer finished");	
    }


	private List<HourlyMeteringDataRaw> groupMeteringDataByDateTime(List<HourlyMeteringDataRaw> meteringDataRaw) {
		Map<Pair<String, LocalDate>, List<HourlyMeteringDataRaw>> meteringDayData = meteringDataRaw.stream()
				.collect(Collectors.groupingBy(m -> Pair.of(m.getExternalCode(), m.getMeteringDate().toLocalDate())));
			
		List<HourlyMeteringDataRaw> meteringData = new ArrayList<>();
		for (Pair<String, LocalDate> pair : meteringDayData.keySet()) {
			Map<Byte, List<HourlyMeteringDataRaw>> meteringHourData =  meteringDayData.get(pair)
				.stream()
				.collect(Collectors.groupingBy(m -> m.getHour()));
			
			for (Byte hour : meteringHourData.keySet()) {
				HourlyMeteringDataRaw h = new HourlyMeteringDataRaw();
				h.setExternalCode(pair.getLeft());
				h.setMeteringDate(pair.getRight().atStartOfDay());
				h.setHour(hour);
				
				h.setStatus( meteringHourData.get(hour).get(0).getStatus() );
				h.setDataSourceCode( meteringHourData.get(hour).get(0).getDataSourceCode() );
				h.setParamCode( meteringHourData.get(hour).get(0).getParamCode() );
				h.setUnitCode( meteringHourData.get(hour).get(0).getUnitCode() );
				h.setWayEntering( meteringHourData.get(hour).get(0).getWayEntering() );
				h.setVal(meteringHourData.get(hour).stream().mapToDouble(m -> m.getVal()).sum());
				meteringData.add(h);
			}
		}			
		
		return meteringData;
	}
	
	
	private LocalDateTime buildEndDateTime() {
		LocalDateTime now = LocalDateTime.now().minusMinutes(15);
		return  LocalDateTime.of(
				now.getYear(),
				now.getMonth(),
				now.getDayOfMonth(),
				now.getHour(),
				45
			).plusHours(1);		
	}
	
	private void updateLoadMeteringInfo(List<HourlyMeteringDataRaw> meteringData, LocalDateTime endDateTime) {
		meteringData.stream()
			.map(t -> t.getExternalCode())
			.distinct()
			.collect(Collectors.toList())
			.forEach(externalCode -> {
	
				LocalDateTime lastLoadedDate = meteringData.stream()
					.filter(p -> p.getExternalCode().equals(externalCode))	
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
	
	
	@Inject
	private MeteringDataQueueService<HourlyMeteringDataRaw> service;

	@Inject
	private MeteringPointService meteringPointService; 
	
	@PersistenceContext(unitName = "bln")
	private EntityManager em;
}
