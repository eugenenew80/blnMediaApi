package kz.kegoc.bln.repository.doc.impl;

import kz.kegoc.bln.entity.doc.DocType;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.doc.DocTypeRepository;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
public class DocTypeRepositoryImpl extends AbstractRepository<DocType>
		implements DocTypeRepository {

	public DocTypeRepositoryImpl() { setClazz(DocType.class); }

	public DocTypeRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
