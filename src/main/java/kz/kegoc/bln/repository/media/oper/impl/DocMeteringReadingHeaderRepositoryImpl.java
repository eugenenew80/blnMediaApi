package kz.kegoc.bln.repository.media.oper.impl;

import kz.kegoc.bln.entity.media.oper.DocMeteringReadingHeader;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.oper.DocMeteringReadingHeaderRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
public class DocMeteringReadingHeaderRepositoryImpl
		extends AbstractRepository<DocMeteringReadingHeader>
				implements DocMeteringReadingHeaderRepository {

	public DocMeteringReadingHeaderRepositoryImpl() { setClazz(DocMeteringReadingHeader.class); }

	public DocMeteringReadingHeaderRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
