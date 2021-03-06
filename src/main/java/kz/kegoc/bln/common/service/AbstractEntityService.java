package kz.kegoc.bln.common.service;

import java.time.*;
import java.util.*;
import javax.validation.*;
import kz.kegoc.bln.common.interfaces.HasDates;
import kz.kegoc.bln.common.interfaces.HasId;
import kz.kegoc.bln.exception.EntityNotFoundException;
import kz.kegoc.bln.exception.InvalidArgumentException;
import kz.kegoc.bln.exception.RepositoryNotFoundException;
import kz.kegoc.bln.common.repository.Repository;

public abstract class AbstractEntityService<T extends HasId> implements EntityService<T> {
    public AbstractEntityService() {}

    public AbstractEntityService(Repository<T> repository) {
        this.repository = repository;
    }
    
    public AbstractEntityService(Repository<T> repository, Validator validator) {
        this(repository);
        this.validator = validator;
    }

        
	public List<T> findAll() {
		if (repository==null)
			throw new RepositoryNotFoundException();
		
		return repository.selectAll();
	}

	public T findById(Long entityId) {
		if (repository==null)
			throw new RepositoryNotFoundException();

		if (entityId==null)
			throw new InvalidArgumentException();
		
		T entity = repository.selectById(entityId);
		if (entity==null)
			throw new EntityNotFoundException(entityId);
		
		return entity;
	}

	public T create(T entity) {
		if (repository==null)
			throw new RepositoryNotFoundException();

		if (entity==null) 
			throw new InvalidArgumentException();
		
		if (entity.getId()!=null)
			throw new InvalidArgumentException(entity);

		if (entity instanceof HasDates)
			((HasDates) entity).setCreateDate(LocalDateTime.now());

		if (validator!=null)
			validate(entity);

		return repository.insert(entity);
	}


	public T update(T entity) {
		if (repository==null)
			throw new RepositoryNotFoundException();

		if (entity==null) 
			throw new InvalidArgumentException();
		
		if (entity.getId()==null) 
			throw new InvalidArgumentException(entity);

		T currentEntity = findById(entity.getId());
		if (entity instanceof HasDates) {
			((HasDates) entity).setCreateDate( ((HasDates)currentEntity).getCreateDate() );
			((HasDates) entity).setLastUpdateDate(LocalDateTime.now());
		}

		if (validator!=null)
			validate(entity);

		return repository.update(entity);
	}


	public boolean delete(Long entityId) {
		if (repository==null)
			throw new RepositoryNotFoundException();

		if (entityId==null)
			throw new InvalidArgumentException();

		findById(entityId);

		return repository.delete(entityId);
	}


	private void validate(T entity) {
		Set<ConstraintViolation<T>> violations =  validator.validate(entity);
		if (violations.size()>0) {
			ConstraintViolation<T> violation = violations.iterator().next();
			throw new ValidationException(violation.getPropertyPath() + ": " + violation.getMessage());
		}
	}

	protected Repository<T> repository;
	private Validator validator;
}
