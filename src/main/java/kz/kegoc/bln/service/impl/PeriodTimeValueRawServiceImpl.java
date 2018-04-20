package kz.kegoc.bln.service.impl;

import kz.kegoc.bln.common.service.AbstractEntityService;
import kz.kegoc.bln.imp.raw.PeriodTimeValueRaw;
import kz.kegoc.bln.repository.PeriodTimeValueRawRepository;
import kz.kegoc.bln.service.PeriodTimeValueRawService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class PeriodTimeValueRawServiceImpl extends AbstractEntityService<PeriodTimeValueRaw> implements PeriodTimeValueRawService {
    private static final Logger logger = LoggerFactory.getLogger(PeriodTimeValueRawServiceImpl.class);

    @Inject
    public PeriodTimeValueRawServiceImpl(PeriodTimeValueRawRepository repository, Validator validator) {
        super(repository, validator);
        this.repository = repository;
    }

    @Override
    public void saveAll(List<PeriodTimeValueRaw> list) {
        long count=0;
        LocalDateTime now = LocalDateTime.now();
        for (PeriodTimeValueRaw pt : list) {
            pt.setCreateDate(now);
            repository.insert(pt);

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
        PeriodTimeValueRawRepository repo = (PeriodTimeValueRawRepository) this.repository;
        repo.getEntityManager().flush();
        repo.getEntityManager().clear();
    }

    private PeriodTimeValueRawRepository repository;
}
