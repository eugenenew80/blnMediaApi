package kz.kegoc.bln.repository.media.doc.impl;

import kz.kegoc.bln.entity.media.doc.DocUnderAccountingCalcLine;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.doc.DocUnderAccountingCalcLineRepository;

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
