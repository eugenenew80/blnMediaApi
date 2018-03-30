package kz.kegoc.bln.repository.impl;

import kz.kegoc.bln.entity.media.Parameter;
import kz.kegoc.bln.common.repository.AbstractRepository;
import kz.kegoc.bln.repository.ParameterRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
public class ParameterRepositoryImpl extends AbstractRepository<Parameter> implements ParameterRepository {
	public ParameterRepositoryImpl() { setClazz(Parameter.class); }

	public ParameterRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
