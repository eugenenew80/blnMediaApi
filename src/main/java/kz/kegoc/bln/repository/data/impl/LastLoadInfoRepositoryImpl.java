package kz.kegoc.bln.repository.data.impl;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import kz.kegoc.bln.entity.data.LastLoadInfo;
import kz.kegoc.bln.repository.common.AbstractRepository;
import kz.kegoc.bln.repository.data.LastLoadInfoRepository;

@Stateless
public class LastLoadInfoRepositoryImpl extends AbstractRepository<LastLoadInfo> implements LastLoadInfoRepository {
	public LastLoadInfoRepositoryImpl() { setClazz(LastLoadInfo.class); }

	public LastLoadInfoRepositoryImpl(EntityManager entityManager) {
		this();
		setEntityManager(entityManager);
	}

	public void mrUpdateLastDate(Long batchId){
		StoredProcedureQuery query = getEntityManager().createStoredProcedureQuery("media_raw_data_proc.mr_last_load_info");
		query.registerStoredProcedureParameter("p_batch_id", Long.class, ParameterMode.IN);
		query.setParameter("p_batch_id", batchId);
		query.execute();
	}

	public void pcUpdateLastDate(Long batchId){
		StoredProcedureQuery query = getEntityManager().createStoredProcedureQuery("media_raw_data_proc.pc_last_load_info");
		query.registerStoredProcedureParameter("p_batch_id", Long.class, ParameterMode.IN);
		query.setParameter("p_batch_id", batchId);
		query.execute();
	}

	public void mrLoad(Long batchId){
		StoredProcedureQuery query = getEntityManager().createStoredProcedureQuery("media_raw_data_proc.mr_proc");
		query.registerStoredProcedureParameter("p_batch_id", Long.class, ParameterMode.IN);
		query.setParameter("p_batch_id", batchId);
		query.execute();
	}

	public void pcLoad(Long batchId){
		StoredProcedureQuery query = getEntityManager().createStoredProcedureQuery("media_raw_data_proc.pc_proc");
		query.registerStoredProcedureParameter("p_batch_id", Long.class, ParameterMode.IN);
		query.setParameter("p_batch_id", batchId);
		query.execute();
	}
}
