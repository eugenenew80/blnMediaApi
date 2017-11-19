package kz.kegoc.bln.repository.media.doc.impl;

import kz.kegoc.bln.entity.media.doc.DocMeterReplacingLine;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.doc.DocMeterReplacingLineRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
public class DocMeterReplacingLineRepositoryImpl
		extends AbstractRepository<DocMeterReplacingLine>
				implements DocMeterReplacingLineRepository {

	public DocMeterReplacingLineRepositoryImpl() { setClazz(DocMeterReplacingLine.class); }

	public DocMeterReplacingLineRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
