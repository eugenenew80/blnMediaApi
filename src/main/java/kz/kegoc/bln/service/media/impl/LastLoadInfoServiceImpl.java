package kz.kegoc.bln.service.media.impl;

import static java.util.stream.Collectors.groupingBy;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import org.apache.commons.lang3.tuple.Pair;

import kz.kegoc.bln.entity.media.LastLoadInfo;
import kz.kegoc.bln.entity.media.day.DayMeteringBalanceRaw;
import kz.kegoc.bln.producer.emcos.reader.helper.MinuteMeteringDataDto;
import kz.kegoc.bln.repository.media.LastLoadInfoRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.media.LastLoadInfoService;

@Stateless
public class LastLoadInfoServiceImpl extends AbstractEntityService<LastLoadInfo> implements LastLoadInfoService {
    
	@Inject
    public LastLoadInfoServiceImpl(LastLoadInfoRepository repository, Validator validator) {
        super(repository, validator);
        this.lastLoadInfoRepository = repository;
    }

	public void updateLastDataLoadDate(List<MinuteMeteringDataDto> minuteMeteringData) {
		Map<Pair<String, String>, List<MinuteMeteringDataDto>> map = minuteMeteringData
			.stream()
			.collect(groupingBy(m -> Pair.of(m.getExternalCode(), m.getParamCode())));		
		
		for (Pair<String, String> pair : map.keySet()) {
			LocalDateTime lastDataLoadDate = map.get(pair)
				.stream()
				.filter(p -> p.getExternalCode().equals(pair.getLeft()) && p.getParamCode().equals(pair.getRight()))
				.map(p -> p.getMeteringDate())
				.max(LocalDateTime::compareTo)
				.orElse(null);
			
			LastLoadInfo l = lastLoadInfoRepository.findByExternalCodeAndParamCode(pair.getLeft(), pair.getRight());
			if (l==null) {
				l = new LastLoadInfo();
				l.setExternalCode(pair.getLeft());
				l.setParamCode(pair.getRight());
			}
			l.setLastLoadDate(lastDataLoadDate);

			if (l.getId()==null)
				repository.insert(l);
			else
				repository.update(l);			
		}
	}	

	public void updateLastBalanceLoadDate(List<DayMeteringBalanceRaw> meteringBalance) {		
		Map<Pair<String, String>, List<DayMeteringBalanceRaw>> map = meteringBalance
				.stream()
				.collect(groupingBy(m -> Pair.of(m.getExternalCode(), m.getParamCode())));		
			
		for (Pair<String, String> pair : map.keySet()) {
			LocalDateTime lastBalanceLoadDate = map.get(pair)
				.stream()
				.filter(p -> p.getExternalCode().equals(pair.getLeft()) && p.getParamCode().equals(pair.getRight()))
				.map(p -> p.getMeteringDate())
				.max(LocalDateTime::compareTo)
				.orElse(null);
			
			LastLoadInfo l = lastLoadInfoRepository.findByExternalCodeAndParamCode(pair.getLeft(), pair.getRight());
			if (l==null) {
				l = new LastLoadInfo();
				l.setExternalCode(pair.getLeft());
				l.setParamCode(pair.getRight());
			}
			l.setLastLoadDate(lastBalanceLoadDate);

			if (l.getId()==null)
				repository.insert(l);
			else
				repository.update(l);			
		}		
	}

	private LastLoadInfoRepository lastLoadInfoRepository;
}
