package kz.kegoc.bln.repository.impl;

import kz.kegoc.bln.imp.raw.PeriodTimeValueRaw;
import kz.kegoc.bln.common.repository.AbstractRepository;
import kz.kegoc.bln.repository.PeriodTimeValueRawRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class PeriodTimeValueRawRepositoryImpl extends AbstractRepository<PeriodTimeValueRaw> implements PeriodTimeValueRawRepository {
	private static final Logger logger = LoggerFactory.getLogger(PeriodTimeValueRawRepositoryImpl.class);

	@Override
	public void saveAll(List<PeriodTimeValueRaw> list) {
		long count=0;
		LocalDateTime now = LocalDateTime.now();
		for (PeriodTimeValueRaw pt : list) {
			pt.setCreateDate(now);
			insert(pt);

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
