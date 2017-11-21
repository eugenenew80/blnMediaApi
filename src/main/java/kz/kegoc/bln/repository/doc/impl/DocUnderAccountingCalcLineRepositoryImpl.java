package kz.kegoc.bln.repository.doc.impl;

import kz.kegoc.bln.entity.doc.DocUnderAccountingCalcLine;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.doc.DocUnderAccountingCalcLineRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
public class DocUnderAccountingCalcLineRepositoryImpl
		extends AbstractRepository<DocUnderAccountingCalcLine>
				implements DocUnderAccountingCalcLineRepository {

	public DocUnderAccountingCalcLineRepositoryImpl() { setClazz(DocUnderAccountingCalcLine.class); }

	public DocUnderAccountingCalcLineRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
