package kz.kegoc.bln.repository.impl;

import kz.kegoc.bln.imp.raw.AtTimeValueRaw;
import kz.kegoc.bln.common.repository.AbstractRepository;
import kz.kegoc.bln.repository.AtTimeValueRawRepository;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
public class AtTimeValueRawRepositoryImpl extends AbstractRepository<AtTimeValueRaw> implements AtTimeValueRawRepository {
    public AtTimeValueRawRepositoryImpl() { setClazz(AtTimeValueRaw.class); }

    public AtTimeValueRawRepositoryImpl(EntityManager entityManager) {
        this();
        setEntityManager(entityManager);
    }
}
