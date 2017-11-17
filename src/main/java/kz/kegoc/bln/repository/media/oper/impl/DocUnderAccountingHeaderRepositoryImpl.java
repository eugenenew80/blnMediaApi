package kz.kegoc.bln.repository.media.oper.impl;

import kz.kegoc.bln.entity.media.oper.DocUnderAccountingHeader;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.oper.DocUnderAccountingHeaderRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
public class DocUnderAccountingHeaderRepositoryImpl
		extends AbstractRepository<DocUnderAccountingHeader>
				implements DocUnderAccountingHeaderRepository {

	public DocUnderAccountingHeaderRepositoryImpl() { setClazz(DocUnderAccountingHeader.class); }

	public DocUnderAccountingHeaderRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
