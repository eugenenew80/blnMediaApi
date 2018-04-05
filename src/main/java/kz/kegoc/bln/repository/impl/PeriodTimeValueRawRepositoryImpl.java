package kz.kegoc.bln.repository.impl;

import kz.kegoc.bln.imp.raw.PeriodTimeValueRaw;
import kz.kegoc.bln.common.repository.AbstractRepository;
import kz.kegoc.bln.repository.MeteringValueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class PeriodTimeValueRawRepositoryImpl extends AbstractRepository<PeriodTimeValueRaw> implements MeteringValueRepository<PeriodTimeValueRaw> {
	private static final Logger logger = LoggerFactory.getLogger(PeriodTimeValueRawRepositoryImpl.class);

	public PeriodTimeValueRaw selectByEntity(PeriodTimeValueRaw entity) {
		return
			getEntityManager().createNamedQuery("PeriodTimeValueRaw.findByEntity", PeriodTimeValueRaw.class)
				.setParameter("sourceMeteringPointCode", entity.getSourceMeteringPointCode())
				.setParameter("meteringDate", 			entity.getMeteringDate())
				.setParameter("sourceUnitCode", 			entity.getSourceUnitCode())
				.setParameter("sourceSystemCode", 		entity.getSourceSystemCode())
				.setParameter("sourceParamCode", 		entity.getSourceParamCode())
			.getResultList()
				.stream()
				.findFirst()
				.orElse(null);
	}

	@Override
	public void saveAll(List<PeriodTimeValueRaw> list) {
		long count=0;
		for (PeriodTimeValueRaw m : list) {
			PeriodTimeValueRaw data = selectByEntity(m);
			if (data == null) {
				m.setCreateDate(LocalDateTime.now());
				insert(m);
			} else {
				m.setId(data.getId());
				m.setCreateDate(data.getCreateDate());
				m.setLastUpdateDate(LocalDateTime.now());
				update(m);
			}
			count++;
			if (count % 1000 == 0) {
				getEntityManager().flush();
				getEntityManager().clear();
				logger.info("Saved records: " + count);
			}
		}
		getEntityManager().flush();
		getEntityManager().clear();
		logger.info("Saved records: " + count);
	}
}
