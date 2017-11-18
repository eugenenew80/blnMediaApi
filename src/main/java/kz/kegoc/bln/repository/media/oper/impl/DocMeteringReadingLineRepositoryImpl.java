package kz.kegoc.bln.repository.media.oper.impl;

import kz.kegoc.bln.entity.media.oper.DocMeteringReadingLine;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.oper.DocMeteringReadingLineRepository;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
public class DocMeteringReadingLineRepositoryImpl
		extends AbstractRepository<DocMeteringReadingLine>
				implements DocMeteringReadingLineRepository {

	public DocMeteringReadingLineRepositoryImpl() { setClazz(DocMeteringReadingLine.class); }

	public DocMeteringReadingLineRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
