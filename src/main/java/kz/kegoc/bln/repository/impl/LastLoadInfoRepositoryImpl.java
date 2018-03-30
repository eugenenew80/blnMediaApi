package kz.kegoc.bln.repository.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import kz.kegoc.bln.entity.media.LastLoadInfo;
import kz.kegoc.bln.common.repository.AbstractRepository;
import kz.kegoc.bln.repository.LastLoadInfoRepository;

@Stateless
public class LastLoadInfoRepositoryImpl extends AbstractRepository<LastLoadInfo> implements LastLoadInfoRepository {
	public LastLoadInfoRepositoryImpl() { setClazz(LastLoadInfo.class); }

	public LastLoadInfoRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}

	public void atUpdateLastDate(Long batchId){
		StoredProcedureQuery query = getEntityManager().createStoredProcedureQuery("media_raw_data_proc.at_last_load_info");
		query.registerStoredProcedureParameter("p_batch_id", Long.class, ParameterMode.IN);
		query.setParameter("p_batch_id", batchId);
		query.execute();
	}

	public void ptUpdateLastDate(Long batchId){
		StoredProcedureQuery query = getEntityManager().createStoredProcedureQuery("media_raw_data_proc.pt_last_load_info");
		query.registerStoredProcedureParameter("p_batch_id", Long.class, ParameterMode.IN);
		query.setParameter("p_batch_id", batchId);
		query.execute();
	}

	public void atLoad(Long batchId){
		StoredProcedureQuery query = getEntityManager().createStoredProcedureQuery("media_raw_data_proc.at_proc");
		query.registerStoredProcedureParameter("p_batch_id", Long.class, ParameterMode.IN);
		query.setParameter("p_batch_id", batchId);
		query.execute();
	}

	public void ptLoad(Long batchId){
		StoredProcedureQuery query = getEntityManager().createStoredProcedureQuery("media_raw_data_proc.pt_proc");
		query.registerStoredProcedureParameter("p_batch_id", Long.class, ParameterMode.IN);
		query.setParameter("p_batch_id", batchId);
		query.execute();
	}
}
