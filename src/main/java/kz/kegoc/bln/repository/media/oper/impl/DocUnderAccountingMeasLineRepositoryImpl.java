package kz.kegoc.bln.repository.media.oper.impl;

import kz.kegoc.bln.entity.media.oper.DocUnderAccountingMeasLine;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.oper.DocUnderAccountingMeasLineRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.List;

@Stateless
public class DocUnderAccountingMeasLineRepositoryImpl
		extends AbstractRepository<DocUnderAccountingMeasLine>
				implements DocUnderAccountingMeasLineRepository {

	public DocUnderAccountingMeasLineRepositoryImpl() { setClazz(DocUnderAccountingMeasLine.class); }

	public DocUnderAccountingMeasLineRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}

	public List<DocUnderAccountingMeasLine> findByHeader(Long headerId) {
		return getEntityManager().createNamedQuery("DocUnderAccountingMeasLine.findMeasByHeader", DocUnderAccountingMeasLine.class)
				.setParameter("headerId", headerId)
				.getResultList();
	}
}
