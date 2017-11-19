package kz.kegoc.bln.repository.media.doc.impl;

import kz.kegoc.bln.entity.media.doc.DocType;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.doc.DocTypeRepository;
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
