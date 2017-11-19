package kz.kegoc.bln.repository.media.doc.impl;

import kz.kegoc.bln.entity.media.doc.GroupMeteringPoint;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.doc.GroupMeteringPointRepository;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

@Stateless
public class GroupMeteringPointRepositoryImpl
		extends AbstractRepository<GroupMeteringPoint>
				implements GroupMeteringPointRepository {

	public GroupMeteringPointRepositoryImpl() { setClazz(GroupMeteringPoint.class); }

	public GroupMeteringPointRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}
}
