package kz.kegoc.bln.repository.media.oper.impl;

import kz.kegoc.bln.entity.media.oper.DocMeteringReadingLine;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.oper.DocMeteringReadingLineRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@Stateless
public class DocMeteringReadingLineRepositoryImpl
		extends AbstractRepository<DocMeteringReadingLine>
				implements DocMeteringReadingLineRepository {

	public DocMeteringReadingLineRepositoryImpl() { setClazz(DocMeteringReadingLine.class); }

	public DocMeteringReadingLineRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}

	public List<DocMeteringReadingLine> findByHeader(Long headerId) {
		return getEntityManager().createNamedQuery("DocMeteringReadingLine.findByHeader", DocMeteringReadingLine.class)
				.setParameter("headerId", headerId)
				.getResultList();
	}
}
