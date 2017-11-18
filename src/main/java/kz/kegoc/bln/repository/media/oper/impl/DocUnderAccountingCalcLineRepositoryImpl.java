package kz.kegoc.bln.repository.media.oper.impl;

import kz.kegoc.bln.entity.media.oper.DocUnderAccountingCalcLine;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.oper.DocUnderAccountingCalcLineRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.List;

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
