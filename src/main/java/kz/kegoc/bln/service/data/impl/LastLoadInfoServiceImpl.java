package kz.kegoc.bln.service.data.impl;

import static java.util.stream.Collectors.groupingBy;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

import kz.kegoc.bln.entity.data.MeteringReadingRaw;
import org.apache.commons.lang3.tuple.Pair;

import kz.kegoc.bln.entity.data.LastLoadInfo;
import kz.kegoc.bln.entity.data.MeasDataRaw;
import kz.kegoc.bln.repository.data.LastLoadInfoRepository;
import kz.kegoc.bln.service.common.AbstractEntityService;
import kz.kegoc.bln.service.data.LastLoadInfoService;

@Stateless
public class LastLoadInfoServiceImpl extends AbstractEntityService<LastLoadInfo> implements LastLoadInfoService {
    
	@Inject
    public LastLoadInfoServiceImpl(LastLoadInfoRepository repository, Validator validator) {
        super(repository, validator);
        this.lastLoadInfoRepository = repository;
    }

	public void updateLastDataLoadDate(List<MeasDataRaw> minuteMeteringData) {
		Map<Pair<String, String>, List<MeasDataRaw>> map = minuteMeteringData
			.stream()
			.collect(groupingBy(m -> Pair.of(m.getSourceMeteringPointCode(), m.getSourceParamCode())));
		
		for (Pair<String, String> pair : map.keySet()) {
			LocalDateTime lastDataLoadDate = map.get(pair)
				.stream()
				.filter(p -> p.getSourceMeteringPointCode().equals(pair.getLeft()) && p.getSourceParamCode().equals(pair.getRight()))
				.map(p -> p.getMeasDate())
				.max(LocalDateTime::compareTo)
				.orElse(null);
			
			LastLoadInfo l = lastLoadInfoRepository.findByExternalCodeAndParamCode(pair.getLeft(), pair.getRight());
			if (l==null) {
				l = new LastLoadInfo();
				l.setSourceMeteringPointCode(pair.getLeft());
				l.setSourceParamCode(pair.getRight());
			}
			l.setLastLoadDate(lastDataLoadDate);

			if (l.getId()==null)
				repository.insert(l);
			else
				repository.update(l);			
		}
	}	

	public void updateLastBalanceLoadDate(List<MeteringReadingRaw> meteringBalance) {
		Map<Pair<String, String>, List<MeteringReadingRaw>> map = meteringBalance
				.stream()
				.collect(groupingBy(m -> Pair.of(m.getSourceMeteringPointCode(), m.getSourceParamCode())));
			
		for (Pair<String, String> pair : map.keySet()) {
			LocalDateTime lastBalanceLoadDate = map.get(pair)
				.stream()
				.filter(p -> p.getSourceMeteringPointCode().equals(pair.getLeft()) && p.getSourceParamCode().equals(pair.getRight()))
				.map(p -> p.getMeteringDate())
				.max(LocalDateTime::compareTo)
				.orElse(null);
			
			LastLoadInfo l = lastLoadInfoRepository.findByExternalCodeAndParamCode(pair.getLeft(), pair.getRight());
			if (l==null) {
				l = new LastLoadInfo();
				l.setSourceMeteringPointCode(pair.getLeft());
				l.setSourceParamCode(pair.getRight());
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