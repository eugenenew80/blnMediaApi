package kz.kegoc.bln.service.impl;

import kz.kegoc.bln.common.service.AbstractEntityService;
import kz.kegoc.bln.imp.raw.AtTimeValueRaw;
import kz.kegoc.bln.repository.AtTimeValueRawRepository;
import kz.kegoc.bln.service.AtTimeValueRawService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;

@Stateless
public class AtTimeValueRawServiceImpl extends AbstractEntityService<AtTimeValueRaw> implements AtTimeValueRawService {
    private static final Logger logger = LoggerFactory.getLogger(AtTimeValueRawServiceImpl.class);

	@Inject
    public AtTimeValueRawServiceImpl(AtTimeValueRawRepository repository, Validator validator) {
        super(repository, validator);
        this.repository = repository;
    }

    @Override
    public void saveAll(List<AtTimeValueRaw> list) {
        long count=0;
        LocalDateTime now = LocalDateTime.now();
        for (AtTimeValueRaw at : list) {
            at.setCreateDate(now);
            repository.insert(at);

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
        AtTimeValueRawRepository repo = (AtTimeValueRawRepository) this.repository;
	    repo.getEntityManager().flush();
        repo.getEntityManager().clear();
    }

    private AtTimeValueRawRepository repository;
}
