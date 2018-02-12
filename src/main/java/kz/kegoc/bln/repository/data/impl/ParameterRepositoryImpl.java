package kz.kegoc.bln.repository.data.impl;

import kz.kegoc.bln.entity.data.Parameter;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.data.ParameterRepository;

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
