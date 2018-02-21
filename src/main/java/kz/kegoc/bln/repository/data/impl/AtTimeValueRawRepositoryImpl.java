package kz.kegoc.bln.repository.data.impl;

import kz.kegoc.bln.entity.data.AtTimeValueRaw;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.data.MeteringValueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class AtTimeValueRawRepositoryImpl extends AbstractRepository<AtTimeValueRaw> implements MeteringValueRepository<AtTimeValueRaw> {
	private static final Logger logger = LoggerFactory.getLogger(AtTimeValueRawRepositoryImpl.class);

	public AtTimeValueRaw selectByEntity(AtTimeValueRaw entity) {
		return
			getEntityManager().createNamedQuery("AtTimeValueRaw.findByEntity", AtTimeValueRaw.class)
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
	public void saveAll(List<AtTimeValueRaw> list) {
		long couunt=0;
		for (AtTimeValueRaw m : list) {
			AtTimeValueRaw data = selectByEntity(m);
			if (data == null) {
				m.setCreateDate(LocalDateTime.now());
				insert(m);
			} else {
				m.setId(data.getId());
				m.setCreateDate(data.getCreateDate());
				m.setLastUpdateDate(LocalDateTime.now());
				update(m);
			}
			couunt++;
			if (couunt % 1000 == 0) {
				getEntityManager().flush();
				getEntityManager().clear();
				logger.info("Saved records: " + couunt);
			}
		}
		getEntityManager().flush();
		getEntityManager().clear();
		logger.info("Saved records: " + couunt);
	}
}
