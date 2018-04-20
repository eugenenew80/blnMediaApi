package kz.kegoc.bln.repository.impl;

import kz.kegoc.bln.imp.raw.PeriodTimeValueRaw;
import kz.kegoc.bln.common.repository.AbstractRepository;
import kz.kegoc.bln.repository.PeriodTimeValueRawRepository;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
public class PeriodTimeValueRawRepositoryImpl extends AbstractRepository<PeriodTimeValueRaw> implements PeriodTimeValueRawRepository {
    public PeriodTimeValueRawRepositoryImpl() { setClazz(PeriodTimeValueRaw.class); }

    public PeriodTimeValueRawRepositoryImpl(EntityManager entityManager) {
        this();
        setEntityManager(entityManager);
    }
}
