package kz.kegoc.bln.repository.media.oper.impl;

import kz.kegoc.bln.entity.media.oper.DocMeterReplacingLine;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.oper.DocMeterReplacingLineRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.List;

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
