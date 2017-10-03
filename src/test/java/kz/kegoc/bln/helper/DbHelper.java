package kz.kegoc.bln.helper;

import java.sql.Connection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DbHelper {
	private EntityManagerFactory entityManagerFactory;
	private EntityManager em;
	private Connection connection = null;

	public DbHelper() {
		entityManagerFactory = Persistence.createEntityManagerFactory("bln_test");
		em = entityManagerFactory.createEntityManager();
		
		org.hibernate.Session session = em.unwrap(org.hibernate.Session.class);
		connection = ((org.hibernate.internal.SessionImpl) session).connection();
		
		/*
		em.getTransaction().begin();
		connection = em.unwrap(Connection.class);
		em.getTransaction().commit();
		*/
	}
	
	public EntityManager getEntityManager() {
		return em;
	}


	public void closeDatabase()  {
		if (entityManagerFactory != null) {
			entityManagerFactory.close();
		}
	}
	
	public void beginTransaction() {
		em.getTransaction().begin();
	}
	
	
	public void commitTransaction() {
		em.getTransaction().commit();
		em.clear();
	}

	
	public void insert(List<DataSetLoader> loaders) throws Exception {
		for (DataSetLoader loader : loaders)
			loader.cleanAndInsert(connection);
	}

	public void delete(List<DataSetLoader> loaders) throws Exception {
		for (DataSetLoader loader : loaders)
			loader.deleteAll(connection);
	}

	public void cleanAndInsert(List<DataSetLoader> loaders) throws Exception {
		for (DataSetLoader loader : loaders)
			loader.cleanAndInsert(connection);
	}
}
