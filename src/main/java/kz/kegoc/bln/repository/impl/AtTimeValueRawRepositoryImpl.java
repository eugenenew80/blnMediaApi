package kz.kegoc.bln.repository.impl;

import kz.kegoc.bln.imp.raw.AtTimeValueRaw;
import kz.kegoc.bln.common.repository.AbstractRepository;
import kz.kegoc.bln.repository.AtTimeValueRawRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class AtTimeValueRawRepositoryImpl extends AbstractRepository<AtTimeValueRaw> implements AtTimeValueRawRepository {
	private static final Logger logger = LoggerFactory.getLogger(AtTimeValueRawRepositoryImpl.class);

	@Override
	public void saveAll(List<AtTimeValueRaw> list) {
		long count=0;
		LocalDateTime now = LocalDateTime.now();
		for (AtTimeValueRaw m : list) {
			m.setCreateDate(now);

			count++;
			if (count % 1000 == 0) {
				flushAndClear();
				logger.info("Saved records: " + count);
			}
		}
		flushAndClear();
		logger.info("Saved records: " + count);
	}

	private void flushAndClear() {
		getEntityManager().flush();
		getEntityManager().clear();
	}
}
