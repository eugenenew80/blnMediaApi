package kz.kegoc.bln.repository.media.data.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import kz.kegoc.bln.entity.media.data.LastLoadInfo;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.media.data.LastLoadInfoRepository;

@Stateless
public class LastLoadInfoRepositoryImpl extends AbstractRepository<LastLoadInfo> implements LastLoadInfoRepository {
	public LastLoadInfoRepositoryImpl() { setClazz(LastLoadInfo.class); }

	public LastLoadInfoRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}

	public LastLoadInfo findByExternalCodeAndParamCode(String externalCode, String paramCode) {
		return
			getEntityManager().createNamedQuery("LastLoadInfo.findByExternalCodeAndParamCode", LastLoadInfo.class)
				.setParameter("externalCode", externalCode)
				.setParameter("paramCode", paramCode)
				.getResultList()
				.stream()
				.findFirst()
				.orElse(null);
	}
}
