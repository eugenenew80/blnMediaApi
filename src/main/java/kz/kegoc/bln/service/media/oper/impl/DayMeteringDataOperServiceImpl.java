package kz.kegoc.bln.service.media.oper.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import com.google.common.collect.BiMap;
import kz.kegoc.bln.ejb.cdi.annotation.ParamCodes;
import kz.kegoc.bln.entity.dict.Meter;
import kz.kegoc.bln.entity.dict.MeteringPoint;
import kz.kegoc.bln.entity.dict.MeteringPointMeter;
import kz.kegoc.bln.entity.media.DataStatus;
import kz.kegoc.bln.entity.media.WayEntering;
import kz.kegoc.bln.entity.media.oper.DayMeteringDataOper;
import kz.kegoc.bln.entity.media.oper.Group;
import kz.kegoc.bln.entity.media.oper.GroupMeteringPoint;
import kz.kegoc.bln.entity.media.raw.DayMeteringBalanceRaw;
import kz.kegoc.bln.repository.media.oper.DayMeteringDataOperRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.oper.DayMeteringDataOperService;
import kz.kegoc.bln.service.media.oper.GroupService;
import kz.kegoc.bln.service.media.raw.MeteringDataRawService;

@Stateless
public class DayMeteringDataOperServiceImpl
		extends AbstractEntityService<DayMeteringDataOper>
        		implements DayMeteringDataOperService {
    
	@Inject
    public DayMeteringDataOperServiceImpl(DayMeteringDataOperRepository repository, Validator validator) {
        super(repository, validator);
        this.dayMeteringDataOperRepository = repository;
    }
	
	public List<DayMeteringDataOper> findByGroup(Long groupId, LocalDateTime operDate) {
		List<MeteringPoint> meteringPointsInGroup = groupService.findById(groupId).getMeteringPoints()
			.stream()
			.map(m -> m.getMeteringPoint())
			.collect(Collectors.toList());
		
		List<DayMeteringDataOper> operData = dayMeteringDataOperRepository.findByDate(operDate.toLocalDate())
			.stream()
			.filter(m -> meteringPointsInGroup.contains(m.getMeteringPoint()))
			.collect(Collectors.toList());
		
		List<MeteringPoint> curMeteringPoints = operData.stream()
			.map(DayMeteringDataOper::getMeteringPoint)
			.distinct()
			.collect(Collectors.toList());
		
		List<DayMeteringDataOper> newOperData = emptyGroup(groupId, operDate)
			.stream()
			.filter(t -> !curMeteringPoints.contains(t.getMeteringPoint()))
			.collect(Collectors.toList());
		
		operData.addAll(newOperData);
		return operData;
	}
	
	
	private List<DayMeteringDataOper> emptyGroup(Long groupId, LocalDateTime operDate) {
		Group group = groupService.findById(groupId);
		if (group==null)
			return Collections.emptyList();
		
		return 
			group.getMeteringPoints().stream()
				.map(GroupMeteringPoint::getMeteringPoint)
				.map(point -> point.getMeters().stream()
                    .map(MeteringPointMeter::getMeter)
                    .map(meter -> paramCodes.keySet().stream()
                        .filter(param -> param.contains("AB") )
                        .map(param -> mapToPoint(point, meter, param, operDate))
                        .collect(Collectors.toList()))
                    .flatMap(p -> p.stream())
                    .collect(Collectors.toList()))
				.flatMap(l -> l.stream())
				.collect(Collectors.toList());
	}
	
	
	private DayMeteringDataOper mapToPoint(MeteringPoint point, Meter meter, String param, LocalDateTime operDate) {
		DayMeteringDataOper d = new DayMeteringDataOper();
		d.setMeteringPoint(point);
		d.setMeter(meter);
		d.setOperDate(operDate.toLocalDate());
		d.setParamCode(param);
		d.setUnitCode("kWh");
		d.setWayEntering(WayEntering.EMCOS);
		d.setDataSourceCode("EMCOS");
		return d;		
	}
	
	
	public List<DayMeteringDataOper> fillByGroup(Long groupId, LocalDateTime operDate) {		
		return 
			findByGroup(groupId, operDate).stream()
				.map(d -> {
					DayMeteringBalanceRaw b = new DayMeteringBalanceRaw();
					b.setMeteringDate(d.getOperDate().minusDays(1).atStartOfDay());
					b.setExternalCode(d.getMeteringPoint().getExternalCode());
					b.setParamCode(d.getParamCode());
					b.setDataSourceCode(d.getDataSourceCode());
					b.setWayEntering(d.getWayEntering());
					b.setUnitCode(d.getUnitCode());
					b.setStatus(DataStatus.RAW);

					DayMeteringBalanceRaw balanceStart = dayMeteringBalanceService.findByEntity(b);
					d.setStartBalance(0d);
					if (balanceStart!=null)
						d.setStartBalance(balanceStart.getVal());
					
					b.setMeteringDate(operDate);
					DayMeteringBalanceRaw balanceEnd = dayMeteringBalanceService.findByEntity(b);
					d.setEndBalance(0d);
					if (balanceEnd!=null)
						d.setEndBalance(balanceEnd.getVal());
						
					d.setDif(Math.round((d.getEndBalance() - d.getStartBalance())*100d ) / 100d);
					return d;
				})
				.collect(Collectors.toList());
	}	
	
	
	private DayMeteringDataOperRepository dayMeteringDataOperRepository;
	
	@Inject
	private GroupService groupService;
	
	@Inject
	private MeteringDataRawService<DayMeteringBalanceRaw> dayMeteringBalanceService;

	@Inject @ParamCodes
    private BiMap<String, String> paramCodes;
}
