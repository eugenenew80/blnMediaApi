package kz.kegoc.bln.repository.media.oper.impl;

import kz.kegoc.bln.entity.media.oper.DocTemplate;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.oper.DocTemplateRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
public class DocTemplateRepositoryImpl
		extends AbstractRepository<DocTemplate>
				implements DocTemplateRepository {

	public DocTemplateRepositoryImpl() { setClazz(DocTemplate.class); }

	public DocTemplateRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
