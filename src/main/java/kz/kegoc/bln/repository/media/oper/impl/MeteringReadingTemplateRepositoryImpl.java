package kz.kegoc.bln.repository.media.oper.impl;

import kz.kegoc.bln.entity.media.oper.MeteringReadingTemplate;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.oper.MeteringReadingTemplateRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
public class MeteringReadingTemplateRepositoryImpl extends AbstractRepository<MeteringReadingTemplate> implements MeteringReadingTemplateRepository {
	public MeteringReadingTemplateRepositoryImpl() { setClazz(MeteringReadingTemplate.class); }

	public MeteringReadingTemplateRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
