package kz.kegoc.bln.repository.media.oper.impl;

import kz.kegoc.bln.entity.media.oper.DocMeterReplacingHeader;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.oper.DocMeterReplacingHeaderRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
public class DocMeterReplacingHeaderRepositoryImpl
		extends AbstractRepository<DocMeterReplacingHeader>
				implements DocMeterReplacingHeaderRepository {

	public DocMeterReplacingHeaderRepositoryImpl() { setClazz(DocMeterReplacingHeader.class); }

	public DocMeterReplacingHeaderRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
