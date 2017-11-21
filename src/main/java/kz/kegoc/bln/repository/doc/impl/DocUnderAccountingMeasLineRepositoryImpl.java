package kz.kegoc.bln.repository.doc.impl;

import kz.kegoc.bln.entity.doc.DocUnderAccountingMeasLine;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.doc.DocUnderAccountingMeasLineRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
public class DocUnderAccountingMeasLineRepositoryImpl
		extends AbstractRepository<DocUnderAccountingMeasLine>
				implements DocUnderAccountingMeasLineRepository {

	public DocUnderAccountingMeasLineRepositoryImpl() { setClazz(DocUnderAccountingMeasLine.class); }

	public DocUnderAccountingMeasLineRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
