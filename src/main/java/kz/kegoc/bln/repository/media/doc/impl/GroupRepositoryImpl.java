package kz.kegoc.bln.repository.media.doc.impl;

import kz.kegoc.bln.entity.media.doc.Group;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.doc.GroupRepository;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
public class GroupRepositoryImpl extends AbstractRepository<Group>
		implements GroupRepository {

	public GroupRepositoryImpl() { setClazz(Group.class); }

	public GroupRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}